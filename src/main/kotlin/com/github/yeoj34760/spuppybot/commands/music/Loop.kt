package com.github.yeoj34760.spuppybot.commands.music

import com.github.yeoj34760.spuppybot.command.Command
import com.github.yeoj34760.spuppybot.command.CommandEvent
import com.github.yeoj34760.spuppybot.command.CommandInfoName
import com.github.yeoj34760.spuppybot.music.GuildManager

object Loop : Command(CommandInfoName.LOOP) {

    override fun execute(event: CommandEvent) {
        val playerControl = GuildManager.playerControls[event.guild.idLong]
        if (playerControl == null || !playerControl.isPlayed()) {
            event.channel.sendMessage("현재 재생 중이지 않네요").queue()
            return
        }

        if (playerControl.isLooped) {
            event.channel.sendMessage("무한 루프를 해체했습니다.").queue()
            playerControl.isLooped = false
        } else {
            event.channel.sendMessage("무한 루프를 적용했습니다.").queue()
            playerControl.isLooped = true
        }
    }
}