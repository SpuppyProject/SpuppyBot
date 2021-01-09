package com.github.yeoj34760.spuppy.bot.commands.music

import com.github.yeoj34760.spuppy.bot.Bot
import com.github.yeoj34760.spuppy.bot.language.Language
import com.github.yeoj34760.spuppy.bot.player.PlayerGuildManager
import com.github.yeoj34760.spuppy.bot.player.PlayerUtil
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent

object Stop : Command(name= "stop", aliases = Bot.commands["stop"] ?: error("umm..")) {
    override suspend fun execute(event: CommandEvent) {
        val control = PlayerUtil.loadPlayerControl(event) ?: return

        control.stop()
        event.send(Language.toText("stop_success", event.author.idLong))
    }
}