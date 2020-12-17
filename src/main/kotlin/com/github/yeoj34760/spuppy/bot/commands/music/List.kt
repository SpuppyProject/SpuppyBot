package com.github.yeoj34760.spuppy.bot.commands.music

import com.github.yeoj34760.spuppy.bot.Bot
import com.github.yeoj34760.spuppy.bot.player.PlayerGuildManager
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent

object List : Command(name="list", alias = Bot.commands["list"] ?: error("umm..")) {
    private const val pageMax = 10
    override suspend fun execute(event: CommandEvent) {
        if (PlayerGuildManager[event.guild] != null
                && !PlayerGuildManager[event.guild]!!.isPlayed()
                || PlayerGuildManager[event.guild]!!.trackList().isEmpty()) {
            event.send("리스트에 텅 비어있어요")
            return
        }

        if (event.args[0].toIntOrNull() == null) {
            event.send("명령어를 잘못 쓰셨어요!")
            return
        }

        val control = PlayerGuildManager[event.guild]!!
        val pageCount = ( control.trackList().count() % pageMax)



    }
}