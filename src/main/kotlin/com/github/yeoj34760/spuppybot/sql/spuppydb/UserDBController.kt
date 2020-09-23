package com.github.yeoj34760.spuppybot.sql.spuppydb

import com.github.yeoj34760.spuppybot.spuppyDBConnection
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController

object UserDBController {
    fun addUser(id: Long) {
        val ps = spuppyDBConnection.prepareStatement("insert into user values (?)")
        ps.setLong(1,id)
        ps.execute()
    }

    fun delUser(id: Long) {
        val ps = spuppyDBConnection.prepareStatement("delete from user where id=?")
        ps.setLong(1, id)
        ps.execute()
    }
    fun checkUser(id: Long): Boolean {
        val ps = spuppyDBConnection.prepareStatement("select exists(select * from user where ?)")
        ps.setLong(1, id)
        val result = ps.executeQuery()
        if (result.next())
            return result.getInt(1) == 1

        return false
    }
}