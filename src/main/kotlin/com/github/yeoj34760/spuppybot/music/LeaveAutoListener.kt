package com.github.yeoj34760.spuppybot.music

import net.dv8tion.jda.api.events.DisconnectEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent
import net.dv8tion.jda.api.hooks.EventListener
import net.dv8tion.jda.api.hooks.ListenerAdapter


/**
 * 봇이 나갔을 때 자동으로 음악을 중지하도록 합니다, 또한 봇이 들어와 있는 음성 채널에 아무도 없으면 자동으로 나가지도록 합니다.
 */
object LeaveAutoListener : ListenerAdapter() {
    override fun onGuildVoiceLeave(event: GuildVoiceLeaveEvent) {

        if (!event.guild.audioManager.isConnected  || event.guild.audioManager.connectedChannel!!.idLong != event.channelLeft.idLong)
            return

        val playerControl = GuildManager[event.guild.idLong] ?: return
        playerControl.isLooped = false
        playerControl.resume()
        if (event.member.user.idLong != event.jda.selfUser.idLong) {
            if (event.channelLeft.members.size <= 1)
                stopMusic(playerControl)
            return
        }

        //봇이 나갔을 경우
        stopMusic(playerControl)
    }

    override fun onGuildVoiceMove(event: GuildVoiceMoveEvent) {
        val playerControl = GuildManager[event.guild.idLong] ?: return
        playerControl.isLooped = false
        playerControl.resume()
        if (event.guild.audioManager.isConnected && event.guild.audioManager.connectedChannel!!.members.size <= 1)
            stopMusic(GuildManager[event.guild.idLong])

    }

   private fun stopMusic(playerControl: PlayerControl?) {
        if (playerControl != null && playerControl.isPlayed())
            playerControl.stop()
    }
}