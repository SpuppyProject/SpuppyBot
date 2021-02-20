package com.github.yeoj34760.spuppy.bot.commands.music

import com.github.yeoj34760.spuppy.bot.Bot
import com.github.yeoj34760.spuppy.utilities.player.PlayerGuildManager
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent

object Shuffle : Command(name = "shuffle", aliases = Bot.commands["shuffle"] ?: error("umm..")) {
    override suspend fun execute(event: CommandEvent) {
        if (PlayerGuildManager[event.guild]?.trackList()?.count()!! > 1) {
            event.send("플레이리스트에 2개이상 음악이 있어야 되요!")
            return
        }

        PlayerGuildManager[event.guild]!!.shuffle()
        event.send("뒤섞였어요!")
    }
}