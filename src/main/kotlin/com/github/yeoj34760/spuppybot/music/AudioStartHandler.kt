package com.github.yeoj34760.spuppybot.music

import com.jagrosh.jdautilities.command.CommandEvent
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.entities.Message

class AudioStartHandler(private val event: CommandEvent,
                        private val message: Message,
                        private val playerControl: PlayerControl,
                        private val num: Int?) : AudioLoadResultHandler {
    //로드 실패시
    override fun loadFailed(exception: FriendlyException) {
        message.editMessage("umm.. 로드 시도해 보았는데 영 좋지 않네요").queue()
        autoLeave()
    }

    override fun trackLoaded(track: AudioTrack) {
        message.editMessage("`${track.info.title}`를(을) 플레이리스트에 추가했습니다.").queue()
        track.userData = event.author
        if (num == null)
            playerControl.playOrAdd(track)
        else
            playerControl.PlayOrAdd(track, num.toInt())


    }

    override fun noMatches() {
        message.editMessage("어.. 음악을 찾지 못 했어요").queue()
        autoLeave()
    }

    override fun playlistLoaded(playlist: AudioPlaylist) {
        playlist.tracks.forEach {
            it.userData = event.author
            playerControl.playOrAdd(it)
        }

        message.editMessage("`${playlist.name}`의 `${playlist.tracks.size}`개 음악을 플레이리스트에 추가했습니다.").queue()
    }

    private fun autoLeave() {
        if (!playerControl.isPaused())
            event.guild.audioManager.closeAudioConnection()
    }
}