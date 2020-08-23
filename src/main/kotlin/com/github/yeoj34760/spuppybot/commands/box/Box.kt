package com.github.yeoj34760.spuppybot.commands.box

import com.github.yeoj34760.spuppybot.Settings
import com.github.yeoj34760.spuppybot.music.GuildManager
import com.github.yeoj34760.spuppybot.other.Util
import com.github.yeoj34760.spuppybot.playerManager
import com.github.yeoj34760.spuppybot.sql.Commands
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController
import com.github.yeoj34760.spuppybot.sql.UserBoxInfo
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
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
                Commands.ADD_BOX -> addBox(event, args)
                Commands.LIST_BOX -> listBox(event)
                Commands.REMOVE_ALL_BOX -> removeAllBox(event)
                Commands.REMOVE_BOX -> removeBox(event, args?.toIntOrNull())
                Commands.COPY_ALL_BOX -> copyAllBox(event)
                Commands.MOVE_BOX -> moveBox(event, args)

            }
        }
    }

    private fun fromArgs(event: MessageReceivedEvent, commands: List<String>): String? {
        try {
            commands.forEach {
                if (event.message.contentRaw.startsWith(Settings.PREFIX + it)) {
                    return event.message.contentRaw.substring(Settings.PREFIX.length + it.length+1)
                }
            }
        }
        catch (e: Exception) {
            return null
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


    /**
     * url이용하여 관련 정보를 얻어서 직렬화로 작업 한 뒤에 base64로 인코딩하여 데이터베이스에 저장합니다.
     * 트랙 정보(UserBoxInfo) -> json(직렬화) -> Base64(인코딩)
     * 데이터베이스에서 트랙 정보얻을 때 반대 반향으로 진행함.
     */
    private fun addBox(event: MessageReceivedEvent, args: String?) {
        if (args == null) {
            event.channel.sendMessage("명령어를 제대로 적어주세요.\n예시: `${Settings.PREFIX}box add https://youtu.be/HZo539SVHJ4`").queue()
            return
        }
        event.channel.sendMessage("검색 중...").queue {
            if (Util.checkURL(args)) {
                playerManager.loadItem(args, object : AudioLoadResultHandler {
                    override fun loadFailed(exception: FriendlyException) {
                        it.editMessage("umm... 불행하게도 로드 실패했어요.").queue()
                    }

                    override fun trackLoaded(track: AudioTrack) {
                        sendBox(event, track)
                        it.editMessage("추가 됨!").queue()
                    }

                    override fun noMatches() {
                        it.editMessage("umm... 유감스럽게도 매치가 읎어요").queue()
                    }

                    override fun playlistLoaded(playlist: AudioPlaylist) {
                        playlist.tracks.forEach { track ->
                            sendBox(event, track)
                        }
                    }

                })
            }
            else {
            val s =    Util.youtubeSearch(args, it)
                s
            }
        }
    }

    private fun sendBox(event: MessageReceivedEvent, track: AudioTrack) {
        val info = UserBoxInfo(
                track.info.title,
                track.info.length,
                track.info.uri,
                event.author.name,
                track.info.author
        )
        val infoBase64 = Base64.getEncoder().encodeToString(Json.encodeToString(info).toByteArray())
        SpuppyDBController.addUserBox(event.author.idLong, infoBase64)
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

    private fun removeBox(event: MessageReceivedEvent, args: Int?) {
        if (args == null) {
            event.channel.sendMessage("명령어를 제대로 써주세요.\n예시: `${Settings.PREFIX}box remove 1`").queue()
            return
        }
        val max = SpuppyDBController.fromMaxNumber(event.author.idLong)

        when (args) {
            !in 1..max -> {
                event.channel.sendMessage("1 ~ $max 입력해주세요.").queue(); return
            }
        }

        SpuppyDBController.delUserBox(event.author.idLong, args)
        event.channel.sendMessage("삭제완료").queue()
    }

    private fun removeAllBox(event: MessageReceivedEvent) {
        SpuppyDBController.delAllUserBox(event.author.idLong)
        event.channel.sendMessage("모두 삭제했습니다.").queue()
    }

    private fun copyAllBox(event: MessageReceivedEvent) {
        //유저가 음성 방에 안 들어와 있을 경우
        if (!event.member!!.voiceState!!.inVoiceChannel()) {
            event.channel.sendMessage("음성 방에 들어와 주세요.").queue()
            return
        }

        if (GuildManager.playerControls[event.guild.idLong] == null || !event.guild.audioManager.isConnected) {
            GuildManager.check(event.guild.audioManager, event.guild.idLong)
            event.guild.audioManager.openAudioConnection(event.member!!.voiceState!!.channel)
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

                override fun playlistLoaded(playlist: AudioPlaylist) {/*정상적으로 데이터를 받았다면 이 함수가 쓸 일이 없음*/
                }
            })
        }
    }

    private fun moveBox(event: MessageReceivedEvent, args: String?) {
        if (args == null) {
            event.channel.sendMessage("명령어를 올 바르게 해주세요.\n예시: `${Settings.PREFIX}box move 1 2`")
            return
        }
        val order1: Int? = args.split(" ")[0].toIntOrNull()
        val order2: Int? = args.split(" ")[1].toIntOrNull()
        val max = SpuppyDBController.fromMaxNumber(event.author.idLong)
        if (order1 == null || order2 == null || order1 > max || order1 < 0 || order2 > max || order2 < 0)
        {
            event.channel.sendMessage("올바르게 숫자를 입력해주세요.").queue()
            return
        }
        SpuppyDBController.moveBox(event.guild.idLong, order1, order2)
        event.channel.sendMessage("성공!").queue()
    }
}