package com.github.yeoj34760.spuppybot.sql.spuppydb

import com.github.yeoj34760.spuppybot.spuppyDBConnection
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController
import java.math.BigInteger

object UserMoneyDBController {
    /**
     * 유저 재산을 확인합니다.
     */
    fun propertyUser(id: Long) : BigInteger {
        val t = spuppyDBConnection.createStatement().executeQuery("select money from user_money where id = $id")
        if (t.next()) {
            return BigInteger(t.getString(1))
        }

        throw Exception("오류 내용: 재산을 확인할 수 없음")

    }

    /**
     * 유저의 재산을 추가
     */
    fun addMoneyUser(id: Long, money: BigInteger) : BigInteger {
        val oldMoney = propertyUser(id)
        val newMoney = oldMoney.add(money)
        spuppyDBConnection.createStatement().execute("update user_money set money=${newMoney} where id=$id")

        return newMoney
    }

    /**
     * 유저의 재산의 수를 뺍니다.
     */
    fun minusMoneyUser(id: Long, money: BigInteger) {
        val nowMoney = propertyUser(id)
        val ps = spuppyDBConnection.prepareStatement("update user_money set money=? where id=?")
        nowMoney.minus(money).toString()
        ps.setString(1, nowMoney.minus(money).toString())
        ps.setLong(2, id)
        ps.executeQuery()
    }

    fun createMoneyUser(id: Long) {
        val ps = spuppyDBConnection.prepareStatement("insert into user_money values (?)")
        ps.setLong(1, id)
        ps.execute()
    }
    fun delMoneyUser(id: Long) {
        val ps = spuppyDBConnection.prepareStatement("delete from user_money where id=?")
        ps.setLong(1, id)
        ps.execute()
    }
    fun checkMoneyUser(id: Long) : Boolean {
        val ps = spuppyDBConnection.prepareStatement("select exists(select * from user_money where ?)")
        ps.setLong(1, id)
        val result = ps.executeQuery()
        if (result.next())
            return result.getInt(1) == 1

        return false
    }
}