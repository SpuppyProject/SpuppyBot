package com.github.yeoj34760.spuppybot.sql.spuppydb

import com.github.yeoj34760.spuppybot.SpuppyDBConnection
import java.sql.Timestamp
import java.util.*

object ReceiveMoneyDBController {
    /**
     * 돈받기 타이머 값을 불러옵니다.
     */
    fun receiveMoneyTimer(id: Long): Long {
        val ps = SpuppyDBConnection().prepareStatement("select timer from user_receive_money_timer where id=?")
        ps.setLong(1, id)
        val t = ps.executeQuery()
        if (t.next()) {
            return t.getTimestamp(1).time
        }

        throw Exception("돈받기 타이머 값 가져오기 실패함")
    }

    /**
     * 돈받기 타이머 갱신합니다.
     */
    fun setupReceiveMoneyTimer(id: Long) {
        val pr = SpuppyDBConnection().prepareStatement("update user_receive_money_timer set timer=? where id=?")
        pr.setLong(2, id)
        pr.setTimestamp(1, Timestamp(Date().time + 60000))
        pr.execute()
    }

    /**
     * 돈받기 타이머 생성합니다.
     */
    fun createReceiveMoneyUser(id: Long) {
        val pr = SpuppyDBConnection().prepareStatement("insert into user_receive_money_timer (id, timer) values (?, ?)")
        pr.setLong(1, id)
        pr.setTimestamp(2, Timestamp(Date().time))
        pr.execute()
    }

    /**
     * 돈받기를 한번이라도 했는지 체크합니다.
     * 안했다면 false
     */
    fun checkReceiveMoneyUser(id: Long): Boolean {
        val ps = SpuppyDBConnection().prepareStatement("select exists(select * from user_receive_money_timer where id=?)")
        ps.setLong(1, id)
        val result = ps.executeQuery()
        if (result.next())
            return result.getInt(1) == 1

        return false
    }
}