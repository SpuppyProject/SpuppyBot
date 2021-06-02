package com.github.yeoj34760.spuppy.bot.commands.gamble

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.utilities.CheckUser
import com.github.yeoj34760.spuppy.utilities.randomGamble
import java.math.BigDecimal

object Half : Command("half", aliases = listOf("half")) {
    override suspend fun execute(event: CommandEvent) {
        val user = CheckUser.databaseUserExist(event) ?: return
        val halfMoney = user.money.div(BigDecimal("2"))
        randomGamble(event, halfMoney)
    }
}