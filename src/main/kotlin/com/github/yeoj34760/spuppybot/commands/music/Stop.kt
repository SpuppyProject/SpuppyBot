package com.github.yeoj34760.spuppybot.commands.music

import com.github.yeoj34760.spuppybot.command.Command
import com.github.yeoj34760.spuppybot.command.CommandEvent
import com.github.yeoj34760.spuppybot.command.CommandInfoName
import com.github.yeoj34760.spuppybot.music.GuildManager.playerControls

/**
 * 음악을 멈출 때 쓰입니다.
 */
object Stop : Command(CommandInfoName.STOP) {
    override fun execute(event: CommandEvent) {
        val id = event.guild.idLong


        //
        if (playerControls[id] == null || playerControls[id]!!.isPlayed()) {
            event.channel.sendMessage("음악을 멈추었어요").queue()
            playerControls[id]!!.stop()
            event.guild.audioManager.closeAudioConnection()
        } else
            event.channel.sendMessage("현재 재생되어 있지 않네요").queue()
    }
}