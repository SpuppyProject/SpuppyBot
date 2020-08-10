package com.github.yeoj34760.spuppybot.music

import com.github.yeoj34760.spuppybot.playerManager
import net.dv8tion.jda.api.managers.AudioManager

object GuildManager {
    val tracks = HashMap<Long, PlayerControl>()

    /**
     * 해당 길드전용 플레이어 컨트롤을 반환합니다. 생성되어 있지 않을 경우 null로 반환합니다.
     */
   operator fun get(id: Long): PlayerControl? = tracks[id]

    /**
     * 해당 길드에 플레이어 컨트롤러가 생성되어 있지 않으면 생성합니다.
     */
    fun check(audioManager: AudioManager, id: Long) {
        val playerControl: PlayerControl
        if (!tracks.containsKey(id)) {
            val player = playerManager.createPlayer()
            //볼륨 값 70로 설정
            player.volume = 70
            //오디오매니저에 플레이어 등록
            audioManager.sendingHandler = PlayerSendHandler(player)
            //해당 길드전용 플레이어컨트롤 생성
            playerControl = PlayerControl(player, audioManager)
            player.addListener(playerControl)
            tracks[id] = playerControl
        }
    }
}