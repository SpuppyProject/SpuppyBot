package com.github.yeoj34760.spuppy.bot.commands.gamble

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.utilities.CheckUser
import com.github.yeoj34760.spuppy.utilities.randomGamble

object AllIn : Command(name="allin", aliases = listOf("allin")) {
    override suspend fun execute(event: CommandEvent) {
        val user = CheckUser.databaseUserExist(event) ?: return

        randomGamble(event, user.money)
    }
}