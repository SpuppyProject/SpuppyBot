package com.github.yeoj34760.spuppybot.music

import com.github.yeoj34760.spuppybot.playerManager
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController
import net.dv8tion.jda.api.managers.AudioManager

object GuildManager {
    val playerControls = HashMap<Long, PlayerControl>()

    /**
     * 해당 길드에 플레이어 컨트롤러가 생성되어 있지 않으면 생성합니다.
     */
    fun check(audioManager: AudioManager, id: Long) {
        val playerControl: PlayerControl
        if (!playerControls.containsKey(id)) {
            val player = playerManager.createPlayer()
            val volume = SpuppyDBController.guildVolume(id)
            if (volume != -1)
            player.volume = volume
            else
                player.volume = 70
            //오디오매니저에 플레이어 등록
            audioManager.sendingHandler = PlayerSendHandler(player)
            //해당 길드전용 플레이어컨트롤 생성
            playerControl = PlayerControl(player, audioManager)
            player.addListener(playerControl)
            playerControls[id] = playerControl
        }
    }
}