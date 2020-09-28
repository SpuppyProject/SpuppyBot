package com.github.yeoj34760.spuppybot.db

import com.github.yeoj34760.spuppybot.db.DB.Connection
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object UserDBController {
    private val logger: Logger = LoggerFactory.getLogger("UserDB")
    fun addUser(id: Long) {
        val ps = Connection().prepareStatement("insert into user values (?)")
        ps.setLong(1, id)
        ps.execute()
        logger.info("[${id}] 유저 아이디를 데이터베이스에 저장함")
    }

    fun delUser(id: Long) {
        val ps = Connection().prepareStatement("delete from user where id=?")
        ps.setLong(1, id)
        ps.execute()
        logger.info("[${id}] 유저 아이디를 데이터베이스에 삭제함")
    }

    fun checkUser(id: Long): Boolean {
        val ps = Connection().prepareStatement("select exists(select * from user where id=?)")
        ps.setLong(1, id)
        val result = ps.executeQuery()
        if (result.next())
            return result.getInt(1) == 1

        logger.warn("[${id}] 찾지 못함")
        return false
    }
}