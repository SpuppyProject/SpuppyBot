package com.github.yeoj34760.spuppybot.money.command.gamble

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.db.UserDB
import com.github.yeoj34760.spuppybot.db.user.info
import com.github.yeoj34760.spuppybot.money.GambleTimer.nowGamblingProbability
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigInteger
import kotlin.random.Random

@CommandSettings(name = "gamble")
object Gamble : Command() {
    private val logger: Logger = LoggerFactory.getLogger("도박")
    override fun execute(event: CommandEvent) {
        if (event.args.isEmpty()) {
            event.channel.sendMessage("숫자를 입력해주세요").queue()
            return
        }

        val temp = "[0-9]*".toRegex()
        val tempStringBuffer = StringBuffer()
        temp.findAll(event.content).forEach {
            tempStringBuffer.append(it.value)
        }
        if (tempStringBuffer.toString().isEmpty()) {
            event.channel.sendMessage("제대로 입력해 주세요").queue()
            return
        }
        if (BigInteger(tempStringBuffer.toString()).compareTo(BigInteger("1000")) == -1) {
            event.channel.sendMessage("1000원이상 입력하셔야 돼요!").queue()
            return
        }


        val userMoney = event.author.info().money
        if (userMoney.compareTo(BigInteger(tempStringBuffer.toString())) == -1) {
            event.channel.sendMessage("현재 돈보다 많은 돈을 지불할 수가 없어요").queue()
            return
        }

        val money = BigInteger(tempStringBuffer.toString())
        val random = Random.nextInt(100)
        logger.info("[${event.author.idLong}] 랜덤 값: ${random}, 현대 도박 확률값: ${nowGamblingProbability}")

        if (random < nowGamblingProbability) {
            event.channel.sendMessage("${tempStringBuffer}원을 벌었어요! 축하드려요!").queue()
            logger.info("[${event.author.idLong}] 도박 성공로 인해 ${money}원을 벌음")
            UserDB(event.author.idLong).moneyUpdate(userMoney.add(money))
            return
        } else {
            event.channel.sendMessage("오 이런! ${tempStringBuffer}원이 잃어버렸어요..").queue()
            logger.info("[${event.author.idLong}] 도박 실패로 인해 ${money}원을 잃음")
            UserDB(event.author.idLong).moneyUpdate(userMoney.minus(money))
            return
        }
    }

}