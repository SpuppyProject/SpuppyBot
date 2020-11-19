package com.github.yeoj34760.spuppy.bot.player

import com.github.yeoj34760.spuppy.command.CommandEvent

object PlayerUtil {
    fun voiceChannelConnect(event: CommandEvent): Boolean {
        try {
            if (event.member?.voiceState?.channel == null)
                return false
            event.guild.audioManager.openAudioConnection(event.member!!.voiceState!!.channel)
            return true
        }
        catch (e: Exception) {
            return false
        }
    }
}