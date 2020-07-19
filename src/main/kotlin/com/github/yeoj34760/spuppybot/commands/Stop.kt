package com.github.yeoj34760.spuppybot.commands

import com.github.yeoj34760.spuppybot.music.GuildManager
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent

/**
 * 음악을 멈출 때 쓰입니다.
 */
object Stop : Command() {
    init {
        super.name = "stop"
        super.aliases = arrayOf("stop", "st", "ㄴㅅ", "스탑", "종료", "ㄴ새ㅔ")
    }
    override fun execute(event: CommandEvent?) {
        val id = event!!.guild.idLong

        if (GuildManager[id] == null || GuildManager.tracks[id]!!.isPlayed()) {
            event.channel.sendMessage("음악을 멈추었어요").queue()
            GuildManager.tracks[id]!!.stop()
            event.guild.audioManager.closeAudioConnection()
        }
        else
            event.channel.sendMessage("현재 재생되어 있지 않네요").queue()
    }
}