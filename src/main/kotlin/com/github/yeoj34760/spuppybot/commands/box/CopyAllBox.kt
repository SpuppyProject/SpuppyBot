package com.github.yeoj34760.spuppybot.commands.box

import com.github.yeoj34760.spuppybot.command.Command
import com.github.yeoj34760.spuppybot.command.CommandEvent
import com.github.yeoj34760.spuppybot.command.CommandInfoName
import com.github.yeoj34760.spuppybot.music.GuildManager
import com.github.yeoj34760.spuppybot.other.Util
import com.github.yeoj34760.spuppybot.playerManager
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack

object CopyAllBox : Command(CommandInfoName.COPY_ALL_BOX) {
    override fun execute(event: CommandEvent) {
        //유저가 음성 방에 안 들어와 있을 경우
        if (!event.member!!.voiceState!!.inVoiceChannel()) {
            event.channel.sendMessage("음성 방에 들어와 주세요.").queue()
            return
        }

        Util.autoConnect(event)

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
}