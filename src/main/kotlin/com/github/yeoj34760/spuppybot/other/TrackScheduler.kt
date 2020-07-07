package com.github.yeoj34760.spuppybot.other

import com.github.natanbc.lavadsp.timescale.TimescalePcmAudioFilter
import com.github.yeoj34760.spuppybot.playerManager
import com.github.yeoj34760.spuppybot.waiter
import com.sedmelluq.discord.lavaplayer.filter.AudioFilter
import com.sedmelluq.discord.lavaplayer.filter.PcmFilterFactory
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import net.dv8tion.jda.api.entities.User
import java.util.*


/**
 * 트랙관리 할 때 쓰입니다.
 * 트랙 추가하거나 트랙 끝났을 때 등등
 */
class TrackScheduler(private val audioPlayer: AudioPlayer) : AudioEventAdapter() {
    var trackQueue: Queue<CustomTrack> = LinkedList()
        private set

    var isPlayed: Boolean = false
        private set

    var nowUser: User? = null

    /**
     * 음악을 시작하거나 추가합니다.
     */
    fun playOrAdd(user: User, audioTrack: AudioTrack) {
        if (trackQueue.isEmpty() && !isPlayed) {
            isPlayed = true
            nowUser = user
            audioPlayer.playTrack(audioTrack)
            return
        }
        trackQueue.add(CustomTrack(user, audioTrack))
    }

    /**
     * 현재 음악을 멈추고 다음 음악으로 재생하며 다음 음악 트랙을 반환합니다.
     * 다음 음악이 없을 경우 null로 반환합니다.
     */
    fun skip(): CustomTrack? =
            if (trackQueue.isNotEmpty()) {
                val track = trackQueue.poll()
                nowUser = track.user
                audioPlayer.playTrack(track.audioTrack)
                track
            } else {
                null
            }

    /**
     * 볼륨 조절합니다.
     */
    fun volume(v: Int) {
        audioPlayer.volume = v
    }

    /**
     * 현재 재생중인 음악을 멈추고 대기중인 음악을 정리합니다
     */
    fun stop() {
        audioPlayer.stopTrack()
        nowUser = null
        trackQueue.clear()
    }

    /**
     * 스피드 조절합니다.
     */
    fun speed(speed: Double) {
        var copyTrack = audioPlayer.playingTrack.makeClone()
        copyTrack.position = audioPlayer.playingTrack.position
        audioPlayer.setFilterFactory { track, format, output ->
            val audioFilter = TimescalePcmAudioFilter(output, format.channelCount, format.sampleRate)
            audioFilter.speed= speed
            listOf(audioFilter)
        }
        audioPlayer.playTrack(copyTrack)
        isPlayed = true
    }

    fun count(): Int = trackQueue.size

    /**
     * 일시정지하거나 다시시작합니다. 일시정지할 경우 false 반환되고 다시시작할 경우 true로 반환합니다.
     */
    fun pause(): Boolean {
        audioPlayer.isPaused = !audioPlayer.isPaused
        return audioPlayer.isPaused
    }

    fun playingTrack(): AudioTrack = audioPlayer.playingTrack

    /**
     * 음악이 끝났을 경우
     */
    override fun onTrackEnd(player: AudioPlayer, track: AudioTrack, endReason: AudioTrackEndReason) {
        if (trackQueue.isEmpty()) {
            isPlayed = false
            return
        }
        val nextTrack = trackQueue.poll()
        isPlayed = true
        nowUser = nextTrack.user
        audioPlayer.playTrack(nextTrack.audioTrack)
    }
}