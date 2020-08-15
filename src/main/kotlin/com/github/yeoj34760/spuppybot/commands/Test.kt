package com.github.yeoj34760.spuppybot.commands

import com.github.yeoj34760.spuppybot.sql.SpuppyDBController
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent

object Test : Command() {
    init {
        super.name = "test"
        super.aliases = arrayOf("t")
    }

    override fun execute(event: CommandEvent) {
    }
}