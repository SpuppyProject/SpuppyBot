package com.github.yeoj34760.spuppybot.commands

import com.github.yeoj34760.spuppybot.music.GuildManager
import com.github.yeoj34760.spuppybot.music.PlayerControl
import com.github.yeoj34760.spuppybot.playerManager
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent

object Loop : Command() {
    init {
        name = "loop"
        aliases = arrayOf("loop", "ㅣ", "ㅣㅐㅔㅔ", "l")
    }
    override fun execute(event: CommandEvent) {
        var playerControl = GuildManager[event.guild.idLong]
        if (playerControl == null || !playerControl.isPlayed()) {
            event.reply("현재 재생 중이지 않네요")
            return
        }

        if (playerControl.isLooped) {
            event.reply("무한 루프를 해체했습니다.")
            playerControl.isLooped = false
        }
        else {
            event.reply("무한 루프를 적용했습니다.")
            playerControl.isLooped = true
        }
    }
}