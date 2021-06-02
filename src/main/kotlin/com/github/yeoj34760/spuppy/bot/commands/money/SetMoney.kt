package com.github.yeoj34760.spuppy.bot.commands.money

import com.github.yeoj34760.spuppy.bot.Bot
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent

object SetMoney : Command(name = "setmoney", aliases = listOf("setmoney")) {
    override suspend fun execute(event: CommandEvent) {
        if (event.author.idLong != Bot.info.ownerId) {
            event.send("이 명령어는 관리자전용입니다.")
            return
        }

//        val id = event.args[0].toLong()
//        val moneyNum = event.args[1].toBigDecimal()
//        val money = MariaUserCache.money(id)
//        val beforeMoney = money.current()
//        MariaUserCache.money(id).set(moneyNum)
//
//        event.send("${event.author.name} 유저의 돈값을 `${beforeMoney}`\$에서 `${moneyNum}`\$로 변경하였습니다.")


    }
}