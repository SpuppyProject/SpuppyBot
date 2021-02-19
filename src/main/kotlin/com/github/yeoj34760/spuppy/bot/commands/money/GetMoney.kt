package com.github.yeoj34760.spuppy.bot.commands.money

import com.github.yeoj34760.spuppy.bot.Bot
import com.github.yeoj34760.spuppy.bot.cache.MariaUserCache
import com.github.yeoj34760.spuppy.bot.database.DatabaseUser
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigInteger

object GetMoney : Command(name = "getmoney", aliases = listOf("돈받기")) {
    override suspend fun execute(event: CommandEvent) {
        val previousMoney = MariaUserCache.currentMoney(event.author.idLong)
        val currentMoney = MariaUserCache.addMoney(event.author.idLong, BigInteger("20"))


        event.send {
            description = "`$20`를 받았습니다."

            addField {
                name = "받기 전"
                value = "```diff\n-${previousMoney}```"
                inline = true
            }

            addField {
                name = "받은 후"
                value = "```diff\n+${currentMoney}```"
                inline = true
            }
        }
    }
}