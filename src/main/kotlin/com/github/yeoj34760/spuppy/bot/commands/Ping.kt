package com.github.yeoj34760.spuppy.bot.commands

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


object Ping : Command(name = "ping", aliases = listOf("ping")) {
    override suspend fun execute(event: CommandEvent) {
        event.send("`${event.ping()}ms` pong!")
    }
}