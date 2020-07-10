package com.github.yeoj34760.spuppybot.music

import com.github.natanbc.lavadsp.timescale.TimescalePcmAudioFilter
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import net.dv8tion.jda.api.managers.AudioManager
import java.util.*


/**
 * 트랙관리 할 때 쓰입니다.
 * 트랙 추가하거나 트랙 끝났을 때 등등
 */
class TrackScheduler(private val audioPlayer: AudioPlayer, private val audioManager: AudioManager) : AudioEventAdapter() {
    var trackQueue: Queue<AudioTrack> = LinkedList<AudioTrack>()
        private set

    /**
     * 대기열 수를 반환합니다.
     */
    fun count(): Int = trackQueue.size

    /**
     * 현재 플레이중인 트랙을 반환합니다.
     */
    fun playingTrack(): AudioTrack = audioPlayer.playingTrack

    /**
     * 현재 플레이 중인지 반환합니다.
     */
    fun isPlayed(): Boolean = audioPlayer.playingTrack != null

    fun isPaused(): Boolean = audioPlayer.isPaused

    /**
     * 음악을 시작하거나 추가합니다.
     */
    fun playOrAdd(audioTrack: AudioTrack) {
        println(audioPlayer.playingTrack)
        if (trackQueue.isEmpty() && !isPlayed()) {
            println("음악 시작됨")
            audioPlayer.playTrack(audioTrack)
            return
        }
        println("추가됨")
        trackQueue.add(audioTrack)
    }

    /**
     * 현재 음악을 멈추고 다음 음악으로 재생하며 다음 음악 트랙을 반환합니다.
     * 다음 음악이 없을 경우 null로 반환합니다.
     */
    fun skip(): AudioTrack? =
            if (trackQueue.isNotEmpty()) {
                val track = trackQueue.poll()
                audioPlayer.playTrack(track)
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
        trackQueue.clear()
    }

    /**
     * 스피드 조절합니다.
     */
    fun speed(speed: Double) {
        var copyTrack = audioPlayer.playingTrack.makeClone()
        copyTrack.position = audioPlayer.playingTrack.position

        audioPlayer.setFilterFactory { _, format, output ->
            val audioFilter = TimescalePcmAudioFilter(output, format.channelCount, format.sampleRate)
            audioFilter.speed = speed
            listOf(audioFilter)
        }
        audioPlayer.playTrack(copyTrack)
    }

    /**
     * 일시정지하거나 다시시작합니다. 일시정지할 경우 false 반환되고 다시시작할 경우 true로 반환합니다.
     */
    /* fun pause(): Boolean {
         audioPlayer.isPaused = !audioPlayer.isPaused
         return audioPlayer.isPaused
     }*/
    fun pause() {
        audioPlayer.isPaused = true
    }

    fun resume() {
        audioPlayer.isPaused = false
    }

    /**
     * 음악이 끝났을 경우
     */
    override fun onTrackEnd(player: AudioPlayer, track: AudioTrack, endReason: AudioTrackEndReason) {
        when {
            endReason == AudioTrackEndReason.REPLACED -> return
            trackQueue.isEmpty() -> trackQueue.isEmpty()
            trackQueue.isNotEmpty() -> audioPlayer.playTrack(trackQueue.poll())
        }
    }
}