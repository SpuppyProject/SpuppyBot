package com.github.yeoj34760.spuppy.utilities.player

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason

class PlayerControl(private val player: AudioPlayer) : AudioEventAdapter() {
    private val trackQueue: MutableList<AudioTrack> = mutableListOf()
    fun play(track: AudioTrack) {
        if (player.playingTrack != null) {
            trackQueue.add(track)
            return
        }
        player.playTrack(track)
    }

    fun stop() {
        trackQueue.clear()
        player.stopTrack()
    }

    fun skip() = player.stopTrack()
    fun isPlayed(): Boolean = player.playingTrack != null
    fun isPaused() = player.isPaused
    fun playingTrack(): AudioTrack? = player.playingTrack
    fun trackList(): List<AudioTrack> = trackQueue
    fun removeTrack(number: Int): AudioTrack = trackQueue.removeAt(number)
    fun shuffle() {
        trackQueue.shuffle()
    }

    override fun onTrackEnd(player: AudioPlayer, track: AudioTrack, endReason: AudioTrackEndReason) {
        when (endReason) {
            AudioTrackEndReason.STOPPED -> nextTrack()
            AudioTrackEndReason.FINISHED -> nextTrack()
        }
    }

    override fun onTrackException(player: AudioPlayer, track: AudioTrack, exception: FriendlyException) {

    }

    private fun nextTrack() {
        player.playTrack(trackQueue.removeFirstOrNull() ?: return)
    }
}