package com.github.yeoj34760.spuppy.bot.commands.gamble

import com.github.yeoj34760.spuppy.bot.Bot
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.utilities.randomGamble
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import kotlin.random.Random

object Gamble : Command("gamble", aliases = listOf("gamble")) {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
    override suspend fun execute(event: CommandEvent) {
        if (event.args.isEmpty()) {
            event.send("명령어 사용설명서\n⤷ `${Bot.info.prefix}도박 <금액>`")
            return
        }

        val num = BigDecimal(event.content.replace("[^0-9]", ""))
        randomGamble(event, num)
    }

}