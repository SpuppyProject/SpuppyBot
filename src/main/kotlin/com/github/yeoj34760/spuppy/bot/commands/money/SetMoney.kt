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

        val id = event.args[0].toLong()
        val user = event.db.cache.getUser(id)

        if (user == null) {
            event.send("입력하신 id는 데이터베이스에 없습니다.")
            return
        }

        val moneyNum = event.args[1].toBigDecimal()
        val beforeMoney = user.money.toString()
        user.money = moneyNum

        event.send("해당 유저의 돈값을 `${beforeMoney}`\$에서 `${moneyNum}`\$로 변경하였습니다.")
    }
}