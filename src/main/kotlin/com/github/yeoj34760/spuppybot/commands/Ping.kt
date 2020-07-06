package com.github.yeoj34760.spuppybot.commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent

object Ping : Command() {
    init {
        super.name = "ping"
        super.help = "핑!"
        super.aliases = arrayOf("ping")
    }
    override fun execute(event: CommandEvent) {
        event.channel.sendMessage(event.args).queue()
    }
}