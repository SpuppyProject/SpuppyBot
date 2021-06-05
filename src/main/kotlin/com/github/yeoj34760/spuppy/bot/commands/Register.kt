package com.github.yeoj34760.spuppy.bot.commands

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.ReactionEvent

object Register : Command(name = "register", aliases = listOf("가입")) {
    override suspend fun execute(event: CommandEvent) {
        val message = event.send {
            description = "가입하시기 전에 개인정보 처리방침을 확인하시길 바랍니다."
        }
        message.addReaction("\uD83D\uDEC1").complete()

        ReactionEvent.add("🛁", message) {
            if (it.userIdLong != event.author.idLong)
                 return@add false

            event.db.cache.addUser(event.author.idLong)
            event.send("데이터베이스에 추가되었습니다!")
            true
        }
    }
}