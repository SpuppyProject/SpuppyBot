package com.github.yeoj34760.spuppybot.money.command.gamble

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.db.UserDB
import com.github.yeoj34760.spuppybot.db.user.info
import com.github.yeoj34760.spuppybot.money.GambleTimer.nowGamblingProbability
import java.math.BigInteger
import kotlin.random.Random

@CommandSettings(name = "gambleall")
object GambleAll : Command() {
    override fun execute(event: CommandEvent) {
        val money = event.author.info.money

        if (money.compareTo(BigInteger("1000")) == -1) {
            event.channel.sendMessage("1000원이상 있어야 돼요!").queue()
            return
        }
        val random = Random.nextInt(100)

        if (random < nowGamblingProbability) {
            event.channel.sendMessage("와 ${money}원을 벌었어요! 축하드려요").queue()
            UserDB(event.author.idLong).moneyUpdate(money.add(money))
            return
        } else {
            event.channel.sendMessage("아쉽게도 ${money}원이 공중분해했어요").queue()
            UserDB(event.author.idLong).moneyUpdate(money.minus(money))
            return
        }
    }
}