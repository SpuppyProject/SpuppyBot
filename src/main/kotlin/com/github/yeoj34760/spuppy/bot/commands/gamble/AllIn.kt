package com.github.yeoj34760.spuppy.bot.commands.gamble

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.utilities.database.cache.MariaUserCache
import com.github.yeoj34760.spuppy.utilities.randomGamble
import java.math.BigDecimal

object AllIn : Command(name="allin", aliases = listOf("allin")) {
    override suspend fun execute(event: CommandEvent) {
        val money = MariaUserCache.money(event.author.idLong)
        randomGamble(event, money.current())
    }
}