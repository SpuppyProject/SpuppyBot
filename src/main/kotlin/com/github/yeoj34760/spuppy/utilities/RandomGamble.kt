package com.github.yeoj34760.spuppy.utilities

import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.utilities.gamble.GambleChanceCache
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import kotlin.random.Random

fun randomGamble(event: CommandEvent, num: BigDecimal) {
    val log: Logger = LoggerFactory.getLogger("RandomGamble")
    val user = event.db.cache.getUser(event.author.idLong)!!

    if (user.money.compareTo(num) == -1) {
        event.send("이 금액으로 도박하시기엔 잔고가 부족합니다.")
        return
    }

    when (Random.nextInt(0, 101)) {
        in 0..GambleChanceCache.currentChance(event.author.idLong) -> {
            log.info("<${event.author.idLong}> gamble is good")
            event.send("성공!")
          user.money =   user.money.add(num)

        }

        else -> {
            log.info("<${event.author.idLong}> gamble is bad")
            event.send("실패!")
            user.money = user.money.minus(num)
        }
    }
}