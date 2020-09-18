package com.github.yeoj34760.spuppybot.commands

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings


@CommandSettings(name="ping", aliases = ["ping", "핑"])
object Ping : Command() {
    override fun execute(event: CommandEvent) {
     event.channel.sendMessage("Pong!\n${event.jda.gatewayPing}ms").queue()
    }
}