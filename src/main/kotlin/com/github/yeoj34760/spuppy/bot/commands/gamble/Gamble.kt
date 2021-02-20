package com.github.yeoj34760.spuppy.bot.commands.gamble

import com.github.yeoj34760.spuppy.bot.Bot
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.utilities.database.cache.MariaUserCache
import com.github.yeoj34760.spuppy.utilities.gamble.GambleChanceCache
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigInteger
import kotlin.random.Random

object Gamble : Command("gamble", aliases = listOf("gamble")) {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
    override suspend fun execute(event: CommandEvent) {
        if (event.args.isEmpty()) {
            event.send("명령어 사용설명서\n⤷ `${Bot.info.prefix}도박 <금액>`")
            return
        }

        val num = BigInteger(event.content.replace("[^0-9]", ""))

        if (MariaUserCache.currentMoney(event.author.idLong).compareTo(num) == -1) {
            event.send("이 금액으로 도박하시기엔 잔고가 부족합니다.")
            return
        }

        when (Random.nextInt(0, 101)) {
            in 0..GambleChanceCache.currentChance(event.author.idLong) -> {
                log.info("<${event.author.idLong}> gamble is good")
                event.send("성공!")
                MariaUserCache.addMoney(event.author.idLong, num)
                GambleChanceCache.resetChance(event.author.idLong)
            }

            else -> {
                log.info("<${event.author.idLong}> gamble is bad")
                event.send("실패!")
                MariaUserCache.minusMoney(event.author.idLong, num)
                GambleChanceCache.increaseChance(event.author.idLong)
            }
        }
    }
}