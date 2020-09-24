package com.github.yeoj34760.spuppybot.commands.game.gamble

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.nowGamblingProbability
import com.github.yeoj34760.spuppybot.sql.spuppydb.UserMoneyDBController
import java.math.BigInteger
import kotlin.random.Random

@CommandSettings(name="gamble")
object Gamble : Command() {
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


        if (UserMoneyDBController.propertyUser(event.author.idLong).compareTo(BigInteger(tempStringBuffer.toString())) == -1) {
            event.channel.sendMessage("현재 돈보다 많은 돈을 지불할 수가 없어요").queue()
            return
        }

        val random = Random.nextInt(100)

        if (random < nowGamblingProbability) {
            event.channel.sendMessage("와 ${tempStringBuffer}원을 벌었어요! 축하드려요").queue()
            UserMoneyDBController.addMoneyUser(event.author.idLong, BigInteger(tempStringBuffer.toString()))
            return
        }
        else {
            event.channel.sendMessage("아쉽게도 ${tempStringBuffer}원이 공중분해했어요").queue()
            UserMoneyDBController.minusMoneyUser(event.author.idLong, BigInteger(tempStringBuffer.toString()))
            return
        }
    }

}