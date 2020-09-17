package com.github.yeoj34760.spuppybot.commands.box

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings

@CommandSettings(name = "box")
object Box : Command() {
    override fun execute(event: CommandEvent) {
        println(event.content)
    }
}