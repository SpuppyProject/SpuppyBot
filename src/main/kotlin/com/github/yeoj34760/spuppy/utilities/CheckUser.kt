package com.github.yeoj34760.spuppy.utilities

import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.database.User

object CheckUser {
    fun databaseUserExist(event: CommandEvent): User? {
        val user = event.db.cache.getUser(event.author.idLong)

        if (user == null)
            event.send("현재 데이터베이스에 등록되어 있지 않아요.. 등록하실려면 가입 명령어를 쳐주세요!")

        return user
    }
}