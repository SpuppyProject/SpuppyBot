package com.github.yeoj34760.spuppybot.db

import com.github.yeoj34760.spuppybot.db.DB.Connection
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigInteger

object UserMoneyDBController {
    /**
     * 유저 재산을 확인합니다.
     */
    private val logger: Logger = LoggerFactory.getLogger("UserMoneyDB")
    fun propertyUser(id: Long): BigInteger {
        val ps = Connection().prepareStatement("select money from user_money where id = ?")
        ps.setLong(1, id)
        val t = ps.executeQuery()
        if (t.next()) {
            logger.info("[${id}] 재산 확인됨")
            return BigInteger(t.getString(1))
        }

        throw Exception("재산을 확인할 수 없음")

    }

    /**
     * 유저의 재산을 추가
     */
    fun addMoneyUser(id: Long, money: BigInteger): BigInteger {
        val oldMoney = propertyUser(id)
        val newMoney = oldMoney.add(money)
        Connection().createStatement().execute("update user_money set money=${newMoney} where id=$id")
        logger.info("[${id}] 유저 지갑에 ${money}원을 추가함")
        return newMoney
    }

    /**
     * 유저의 재산의 수를 뺍니다.
     */
    fun minusMoneyUser(id: Long, money: BigInteger) {
        val nowMoney = propertyUser(id)
        val ps = Connection().prepareStatement("update user_money set money=? where id=?")
        nowMoney.minus(money).toString()
        ps.setString(1, nowMoney.minus(money).toString())
        ps.setLong(2, id)
        logger.info("[${id}] 유저 지갑에 ${money}원을 뺌")
        ps.executeQuery()
    }

    fun createMoneyUser(id: Long) {
        val ps = Connection().prepareStatement("insert into user_money values (?, 50000)")
        ps.setLong(1, id)
        logger.info("[${id}] 돈시스템에 유저 아이디 생성함")
        ps.execute()
    }

    fun delMoneyUser(id: Long) {
        val ps = Connection().prepareStatement("delete from user_money where id=?")
        ps.setLong(1, id)
        logger.info("[${id}] 돈시스템에 유저 아이디 삭제함")
        ps.execute()
    }

    fun checkMoneyUser(id: Long): Boolean {
        val ps = Connection().prepareStatement("select exists(select * from user_money where id=?)")
        ps.setLong(1, id)
        val result = ps.executeQuery()
        if (result.next())
            return result.getInt(1) == 1

        return false
    }
}