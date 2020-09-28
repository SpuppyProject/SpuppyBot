package com.github.yeoj34760.spuppybot.music.command


import com.github.yeoj34760.spuppybot.music.GuildManager
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings


@CommandSettings(name = "loop")
object Loop : Command() {

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