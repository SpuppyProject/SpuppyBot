package com.github.yeoj34760.spuppybot.commands.box

import com.github.yeoj34760.spuppybot.Settings
import com.github.yeoj34760.spuppybot.other.Util
import com.github.yeoj34760.spuppybot.playerManager
import com.github.yeoj34760.spuppybot.sql.Commands
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

object Box : ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (SpuppyDBController.checkCommandGroup("box", event.message.contentRaw) && !SpuppyDBController.checkUser(event.author.idLong))
            event.channel.sendMessage("박스 기능을 이용하시려면 ₩?가입₩로 입력하셔서 가입해주세요")

        if (SpuppyDBController.checkCommand(Commands.BOX, event.message.contentRaw))
            event.channel.sendMessage("test")
        if (SpuppyDBController.checkCommand(Commands.ADD_BOX, event.message.contentRaw)) {

            SpuppyDBController.commandFromList(Commands.ADD_BOX).forEach {
                if (event.message.contentRaw.startsWith(Settings.PREFIX + it)) {
                    addBox(event, event.message.contentRaw.substring(Settings.PREFIX.length + it.length).replace(" ", ""))
                    return
                }
            }
        }
    }

    private fun addBox(event: MessageReceivedEvent, args: String) {
        event.channel.sendMessage("검색 중...").queue {
            if (Util.checkURL(args)) {
                playerManager.loadItem(args, object : AudioLoadResultHandler {
                    override fun loadFailed(exception: FriendlyException) {
                        it.editMessage("umm... 불행하게도 로드 실패했어요.").queue()
                    }

                    override fun trackLoaded(track: AudioTrack) {
                        SpuppyDBController.addUserBox(event.author.idLong, track.info.title, track.info.uri)
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
}