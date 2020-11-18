package com.github.yeoj34760.spuppy.bot.commands.music

import com.github.yeoj34760.spuppy.bot.Bot
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent

object Play : Command(name = "play", alias = Bot.commands["play"] ?: error("umm..")) {
    override fun execute(event: CommandEvent) {
        event.channel.sendMessage("test: Play object").complete()
    }
}