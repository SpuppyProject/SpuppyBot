package com.github.yeoj34760.spuppy.bot.commands.music

import com.github.yeoj34760.spuppy.bot.Bot
import com.github.yeoj34760.spuppy.bot.player.PlayerUtil
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import kotlinx.coroutines.delay
import java.lang.reflect.Type

object NowPlay : Command(name = "nowPlay", alias = Bot.commands["nowplay"] ?: error("umm..")) {
    override suspend fun execute(event: CommandEvent) {
        val control = PlayerUtil.loadPlayerControl(event) ?: return
        val first = control.trackList().first()

    }

}