package com.github.yeoj34760.spuppy.bot.commands.money

import com.github.yeoj34760.spuppy.bot.Bot
import com.github.yeoj34760.spuppy.bot.DiscordColor
import com.github.yeoj34760.spuppy.bot.cache.MariaUserCache
import com.github.yeoj34760.spuppy.bot.database.DatabaseUser
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import org.jetbrains.exposed.sql.transactions.transaction

object Money : Command(name = "money", aliases = listOf("돈")) {
    override suspend fun execute(event: CommandEvent) {
        event.send {
            color = DiscordColor.GREEN.rpb
            addField {
                name = "돈"
                value = "```$${MariaUserCache.currentMoney(event.author.idLong)}```"
            }
        }
    }
}