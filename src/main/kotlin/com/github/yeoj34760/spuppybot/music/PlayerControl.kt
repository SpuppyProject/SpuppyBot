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
class PlayerControl(private val audioPlayer: AudioPlayer, private val audioManager: AudioManager) : AudioEventAdapter() {
    var trackQueue: Queue<AudioTrack> = LinkedList<AudioTrack>()
        private set

    var isLooped: Boolean = false

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

    /**
     * 일시정지되었는지 체크합니다. 일시정지가 되었을 경우 true
     */
    fun isPaused(): Boolean = audioPlayer.isPaused

    /**
     * 음악을 시작하거나 추가합니다.
     */
    fun playOrAdd(audioTrack: AudioTrack) {
        if (trackQueue.isEmpty() && !isPlayed()) {
            audioPlayer.playTrack(audioTrack)
            return
        }

        trackQueue.add(audioTrack)
    }

    fun playOrAdd(audioTrack: AudioTrack, num: Int) {
        if (trackQueue.isEmpty() && !isPlayed()) {
            audioPlayer.playTrack(audioTrack)
            return
        }

        trackQueue.add(audioTrack, num)
    }

    /**
     * 특정 번호에 저장합니다.
     * [1,2,3,4,5,6], 9 -> [1,2,3,9,4,5,6]
     */
    private fun Queue<AudioTrack>.add(track: AudioTrack, num: Int) {
        for (x in 1 until num)
            this.add(this.poll())

        this.add(track)

        for (x in num until this.size)
            this.add(this.poll())
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
            } else null

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
     * 일시정지합니다.
     */
    fun pause() {
        audioPlayer.isPaused = true
    }

    /**
     * 다시 시작합니다.
     */
    fun resume() {
        audioPlayer.isPaused = false
    }

    /**
     * 음악이 끝났을 경우
     */
    override fun onTrackEnd(player: AudioPlayer, track: AudioTrack, endReason: AudioTrackEndReason) {
        when {
            //음악을 다 끝나지 않고 종료할 경우
            endReason == AudioTrackEndReason.REPLACED -> return
            isLooped -> audioPlayer.playTrack(track.makeClone())
            //남은 음악이 없을 경우
            trackQueue.isEmpty() -> audioManager.closeAudioConnection()
            trackQueue.isNotEmpty() -> audioPlayer.playTrack(trackQueue.poll())
        }
    }
}