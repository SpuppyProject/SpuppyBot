package com.github.yeoj34760.spuppybot.money.command.gamble

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.db.UserDB
import com.github.yeoj34760.spuppybot.db.user.info
import com.github.yeoj34760.spuppybot.money.GambleTimer.nowGamblingProbability
import java.math.BigInteger
import javax.management.MalformedObjectNameException
import kotlin.random.Random

@CommandSettings(name = "gambleall")
object GambleAll : Command() {
    override fun execute(event: CommandEvent) {
        val money = event.author.info().money

        if (money.compareTo(BigInteger("1000")) == -1) {
            event.channel.sendMessage("1000원이상 있어야 돼요!").queue()
            return
        }
        val random = Random.nextInt(100)

        if (random < nowGamblingProbability) {
            event.channel.sendMessage("${money}원을 벌었어요! 축하드려요!").queue()
            UserDB(event.author.idLong).moneyUpdate(money.add(money))
            return
        } else {
            event.channel.sendMessage("오 이런! ${money}원이 잃어버렸어요..").queue()
            UserDB(event.author.idLong).moneyUpdate(money.minus(money))
            return
        }
    }
}