package com.github.yeoj34760.spuppybot.music

import com.github.yeoj34760.spuppybot.db.GuildDB
import com.github.yeoj34760.spuppybot.playerManager
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import net.dv8tion.jda.api.managers.AudioManager

object GuildManager {
    val playerControls = HashMap<Long, PlayerControl>()

    /**
     * 해당 길드에 플레이어 컨트롤러가 생성되어 있지 않으면 생성합니다.
     */
    fun check(audioManager: AudioManager, id: Long) {
        if (!playerControls.containsKey(id))
            createPlayerControl(audioManager, id)
    }

    private fun createPlayerControl(audioManager: AudioManager, id: Long) {
        val player = createPlayer(id)
        //오디오매니저에 플레이어 등록
        audioManager.sendingHandler = PlayerSendHandler(player)
        //해당 길드전용 플레이어컨트롤 생성
        val playerControl = PlayerControl(player, audioManager)
        player.addListener(playerControl)
        playerControls[id] = playerControl
    }

    private fun createPlayer(id: Long): AudioPlayer {
        val player = playerManager.createPlayer()
        when (val volume = GuildDB.volume(id)) {
            -1 -> player.volume = 70
            else -> player.volume = volume
        }

        return player
    }
}