package com.github.yeoj34760.spuppybot.commands.box

import com.github.yeoj34760.spuppybot.Settings
import com.github.yeoj34760.spuppybot.music.GuildManager
import com.github.yeoj34760.spuppybot.other.Util
import com.github.yeoj34760.spuppybot.playerManager
import com.github.yeoj34760.spuppybot.sql.Commands
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController
import com.github.yeoj34760.spuppybot.sql.UserBoxInfo
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioTrack
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.util.*


object Box : ListenerAdapter() {


    override fun onMessageReceived(event: MessageReceivedEvent) {

        val boxCommands = SpuppyDBController.fromGroup("box")


        boxCommands.keys.forEach { key ->
            val commands = boxCommands[key]!!
            if (!commandCheck(event, commands))
                return@forEach

            val args: String? = fromArgs(event, commands)
            when (key) {
                Commands.ADD_BOX -> addBox(event, args!!)
                Commands.LIST_BOX -> listBox(event)
                Commands.REMOVE_ALL_BOX -> removeAllBox(event)
                Commands.REMOVE_BOX -> removeBox(event, args!!.toInt())
                Commands.COPY_ALL_BOX -> copyAllBox(event)

            }
        }
    }

    private fun fromArgs(event: MessageReceivedEvent, commands: List<String>): String? {
        commands.forEach {
            if (event.message.contentRaw.startsWith(Settings.PREFIX + it)) {
                return event.message.contentRaw.substring(Settings.PREFIX.length + it.length).replace(" ", "")
            }
        }
        return null
    }

    private fun commandCheck(event: MessageReceivedEvent, commands: List<String>): Boolean {
        commands.forEach {
            if (event.message.contentRaw.startsWith(Settings.PREFIX + it))
                return true
        }

        return false
    }


    private fun addBox(event: MessageReceivedEvent, args: String) {
        event.channel.sendMessage("검색 중...").queue {
            if (Util.checkURL(args)) {
                playerManager.loadItem(args, object : AudioLoadResultHandler {
                    override fun loadFailed(exception: FriendlyException) {
                        it.editMessage("umm... 불행하게도 로드 실패했어요.").queue()
                    }

                    override fun trackLoaded(track: AudioTrack) {
                        val info = UserBoxInfo(
                                track.info.title,
                                track.info.length,
                                track.info.uri,
                                event.author.name,
                                track.info.author
                        )
                     val infoBase64 =   Base64.getEncoder().encodeToString(Json.encodeToString(info).toByteArray())
                        SpuppyDBController.addUserBox(event.author.idLong, infoBase64)
                        it.editMessage("추가 됨!").queue()
                    }

                    override fun noMatches() {
                        it.editMessage("umm... 유감스럽게도 매치가 읎어요").queue()
                    }

                    override fun playlistLoaded(playlist: AudioPlaylist) {
                        it.editMessage("test").queue()
                    }

                })
            }
        }
    }

    private fun listBox(event: MessageReceivedEvent) {
        val listTemp: StringBuffer = StringBuffer()
        var userBoxs = SpuppyDBController.fromUserBox(event.author.idLong)

        if (userBoxs.isEmpty()) {
            event.channel.sendMessage("뭔가 엄청난 걸 보여주려고했지만 박스에 아무 것도 없네요.").queue()
        }
        for (x in 1..userBoxs.size) {
            val userBox = userBoxs.stream().filter { it.order == x }.findAny().get()
            listTemp.append("${userBox.order}. ${userBox.info.title}\n\n")

        }

        val embed = EmbedBuilder()
                .setAuthor(event.author.name, null, event.author.avatarUrl)
                .setTitle("`${event.author.name}`의 박스")
                .setDescription(listTemp.toString())
                .build()
        event.channel.sendMessage(embed).queue()
    }

    private fun removeBox(event: MessageReceivedEvent, args: Int) {
        val max = SpuppyDBController.fromMaxNumber(event.author.idLong)

        when (args) {
            !in 1..max -> {
                event.channel.sendMessage("1 ~ $max 입력해주세요.").queue(); return
            }
        }

        SpuppyDBController.delUserBox(event.author.idLong, args)
        //재정렬
        SpuppyDBController.connection.createStatement().execute("update user_box set `order` = `order` - 1 where id = ${event.author.idLong} and order > $args")
        event.channel.sendMessage("삭제완료").queue()
    }

    private fun removeAllBox(event: MessageReceivedEvent) {
        SpuppyDBController.delAllUserBox(event.author.idLong)
        event.channel.sendMessage("모두 삭제했습니다.").queue()
    }

    private fun copyAllBox(event: MessageReceivedEvent) {
        if (GuildManager.playerControls[event.guild.idLong] == null) {
            event.channel.sendMessage("오류 발생").queue()
        }

        SpuppyDBController.fromUserBox(event.author.idLong).forEach {
            playerManager.loadItem(it.info.url, object : AudioLoadResultHandler {
                override fun loadFailed(exception: FriendlyException) {
                    event.channel.sendMessage("오류 발생").queue()
                }

                override fun trackLoaded(track: AudioTrack) {
                    track.userData = event.author
                    GuildManager.playerControls[event.guild.idLong]?.playOrAdd(track)
                }

                override fun noMatches() {
                    event.channel.sendMessage("오류 발생").queue()
                }

                override fun playlistLoaded(playlist: AudioPlaylist) {}
            })
        }
    }
}