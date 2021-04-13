package com.github.yeoj34760.spuppy.bot.commands.money

import com.github.yeoj34760.spuppy.utilities.database.cache.MariaUserCache
import com.github.yeoj34760.spuppy.utilities.money.MoneyCoolDown
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import java.math.BigDecimal

object GetMoney : Command(name = "getmoney", aliases = listOf("돈받기")) {
    override suspend fun execute(event: CommandEvent) {

        if (MoneyCoolDown.check(event.author.idLong)) {
            event.send("돈 받으려면 ${MoneyCoolDown.remainingTime(event.author.idLong) / 1000}초를 기다려야합니다.")
            return
        }

        val money = MariaUserCache.money(event.author.idLong)
        val currentMoney = money.current()
        money += BigDecimal("20")

        MoneyCoolDown.start(event.author.idLong)
        event.send {
            description = "`$20`를 받았습니다."

            addField {
                name = "받기 전"
                value = "```diff\n+${currentMoney}```"
                inline = true
            }

            addField {
                name = "받은 후"
                value = "```diff\n+${money.current()}```"
                inline = true
            }
        }
    }
}