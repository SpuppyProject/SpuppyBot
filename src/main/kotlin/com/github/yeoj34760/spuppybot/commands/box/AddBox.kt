package com.github.yeoj34760.spuppybot.commands.box

import com.github.yeoj34760.spuppybot.Settings
import com.github.yeoj34760.spuppybot.other.Util
import com.github.yeoj34760.spuppybot.playerManager
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

object AddBox : BasicBox() {
    override fun execute(event: MessageReceivedEvent) {
//        if (args == null) {
//            event.channel.sendMessage("명령어를 제대로 적어주세요.\n예시: `${Settings.PREFIX}box add https://youtu.be/HZo539SVHJ4`").queue()
//            return
//        }
//        event.channel.sendMessage("검색 중...").queue {
//            if (Util.checkURL(args)) {
//                playerManager.loadItem(args, object : AudioLoadResultHandler {
//                    override fun loadFailed(exception: FriendlyException) {
//                        it.editMessage("umm... 불행하게도 로드 실패했어요.").queue()
//                    }
//
//                    override fun trackLoaded(track: AudioTrack) {
//                        sendBox(event, track)
//                        it.editMessage("추가 됨!").queue()
//                    }
//
//                    override fun noMatches() {
//                        it.editMessage("umm... 유감스럽게도 매치가 읎어요").queue()
//                    }
//
//                    override fun playlistLoaded(playlist: AudioPlaylist) {
//                        playlist.tracks.forEach { track ->
//                            sendBox(event, track)
//                        }
//                    }
//                })
//            }
//            else {
//                Util.youtubeSearch(args, it)
//            }
//        }
    }
}