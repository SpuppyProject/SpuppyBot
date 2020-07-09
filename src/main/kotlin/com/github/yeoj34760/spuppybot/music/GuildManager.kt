package com.github.yeoj34760.spuppybot.music

import com.github.yeoj34760.spuppybot.playerManager
import net.dv8tion.jda.api.entities.VoiceChannel
import net.dv8tion.jda.api.managers.AudioManager

object GuildManager {
    val tracks = HashMap<Long, TrackScheduler>()

    val channels = HashMap<Long, VoiceChannel?>()

    fun isTrackCreated(id: Long): Boolean = tracks.containsKey(id)

    fun get(id: Long): TrackScheduler = tracks[id]!!

    /**
     * 해당 길드에 트랙 스캐율러가 생성되어 있지 않으면 생성합니다.
     */
    fun check(audioManager: AudioManager, id: Long) {
        val trackScheduler: TrackScheduler
        if (!tracks.containsKey(id)) {
            val player = playerManager.createPlayer()
            player.volume = 85
            audioManager.sendingHandler = PlayerSendHandler(player)
            trackScheduler = TrackScheduler(player)
            player.addListener(trackScheduler)
            tracks[id] = trackScheduler
        }
    }

    var connected = false
}