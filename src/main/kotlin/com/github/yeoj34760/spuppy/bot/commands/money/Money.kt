package com.github.yeoj34760.spuppy.bot.commands.money

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.utilities.CheckUser
import com.github.yeoj34760.spuppy.utilities.enhance.EmbedColor

object Money : Command(name = "money", aliases = listOf("돈")) {
    override suspend fun execute(event: CommandEvent) {
        val user = CheckUser.databaseUserExist(event) ?: return

        event.send {
            color = EmbedColor.GREEN.rgb
            addField {
                name = "돈"
                value = "```$${user.money}```"
            }
        }
    }
}