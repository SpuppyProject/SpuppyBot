package com.github.yeoj34760.spuppybot.commands

import com.github.yeoj34760.spuppybot.other.GuildManager
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent

/**
 * 음악을 멈출 때 쓰입니다.
 */
object Stop : Command() {
    init {
        super.name = "stop"
        super.aliases = arrayOf("stop")
    }
    override fun execute(event: CommandEvent?) {
        val id = event!!.guild.idLong

        if (GuildManager.isTrackCreated(id) || GuildManager.tracks[id]!!.isPlayed) {
            event.channel.sendMessage("음악을 멈춥니다.").queue()
            GuildManager.tracks[event.guild.idLong]!!.stop()
            event.guild.audioManager.closeAudioConnection()
        }
        else
            event.channel.sendMessage("현재 재생되어 있지 않습니다").queue()
    }
}