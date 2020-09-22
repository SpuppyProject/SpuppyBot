package com.github.yeoj34760.spuppybot.sql

import com.github.yeoj34760.spuppy.command.Commands
import com.github.yeoj34760.spuppybot.item.MarketItem
import com.github.yeoj34760.spuppybot.item.UserItem
import com.github.yeoj34760.spuppybot.other.Util
import com.github.yeoj34760.spuppybot.settings
import com.github.yeoj34760.spuppybot.sql.userbox.UserBox
import java.math.BigInteger
import java.sql.DriverManager
import java.sql.Timestamp
import java.util.*

object SpuppyDBController {
    val connection =
            DriverManager.getConnection(
                    settings.spuppydb.url,
                    settings.spuppydb.user,
                    settings.spuppydb.password)



    fun fromCommands(): List<Commands> {
        var temp = mutableListOf<Commands>()
        val t = connection.createStatement().executeQuery("select name, command from command")
        while (t.next()) {
            if (checkCommands(temp, t.getString(1)))
                fromCommands(temp, t.getString(1))!!.aliases!!.add(t.getString(2))
            else {
                var tempCommands = Commands(t.getString(1))
                tempCommands.aliases.add(t.getString(2))
                temp.add(tempCommands)
            }
        }

        return temp;
    }

    private fun checkCommands(commandsList: List<Commands>, name: String): Boolean {
        for (commands in commandsList) {
            if (commands.name == name)
                return true
        }

        return false
    }

    private fun fromCommands(commandsList: List<Commands>, name: String): Commands? {
        for (commands in commandsList) {
            if (commands.name == name)
                return commands
        }

        return null
    }

    fun addUser(id: Long) = add(id, "user")
    fun delUser(id: Long) = del(id, "user")
    fun checkUser(id: Long): Boolean = check(id, "user")



    fun addMoneyUser(id: Long) = add(id, "user_money")
    fun delMoneyUser(id: Long) = del(id, "user_money")
    fun checkMoneyUser(id: Long) = check(id, "user_money")

    /**
     * 유저 재산을 확인합니다.
     */
    fun propertyUser(id: Long) : BigInteger {
        val t = connection.createStatement().executeQuery("select money from user_money where id = $id")
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
        connection.createStatement().execute("update user_money set money=${newMoney} where id=$id")

        return newMoney
    }


    /**
     * 돈받기 타이머 값을 불러옵니다.
     */
    fun receiveMoneyTimer(id: Long): Long {
        val t = connection.createStatement().executeQuery("select timer from user_receive_money_timer where id = $id")
        if (t.next()) {
            return t.getTimestamp(1).time
        }

        throw Exception("돈받기 타이머 값 가져오기 실패함")
    }

    /**
     * 돈받기 타이머 갱신합니다.
     */
    fun setupReceiveMoneyTimer(id: Long) {
        val pr = connection.prepareStatement("update user_receive_money_timer set timer=? where id=?")
        pr.setLong(2, id)
        pr.setTimestamp(1, Timestamp(Date().time + 60000))
        pr.executeQuery()
    }

    /**
     * 돈받기 타이머 생성합니다.
     */
    fun createReceiveMoneyUser(id: Long) {
        val pr = connection.prepareStatement("insert into user_receive_money_timer (id, timer) values (?, ?)")
        pr.setLong(1, id)
        pr.setTimestamp(2, Timestamp(Date().time))
        pr.executeQuery()
    }

    /**
     * 유저 재산이 등록되어 있는지 확인합니다.
     */
    fun checkReceiveMoneyUser(id: Long) = check(id, "user_receive_money_timer")


    /**
     * 유저의 재산의 수를 뺍니다.
     */
    fun minusMoneyUser(id: Long, money: BigInteger) {
        val nowMoney = propertyUser(id)
            val ps = connection.prepareStatement("update user_money set money=? where id=?")
        nowMoney.minus(money).toString()
        ps.setString(1, nowMoney.minus(money).toString())
        ps.setLong(2, id)
                ps.executeQuery()
    }

    /**
     * 마트에 있는 아이템들을 반환합니다.
     */
    fun marketItemList() : List<MarketItem> {
     val t=   connection.createStatement().executeQuery("select * from market_item")
        val temp = mutableListOf<MarketItem>()
        while (t.next()) {
            val tempItem = MarketItem(
                    t.getString(1),
                    t.getLong(2),
                    t.getInt(3),
                    t.getInt(4)
            )
            temp.add(tempItem)
        }

        return temp
    }

    /**
     * 마켓에 있는 아이템 갯수를 하나뺍니다.
     */
    fun minusMarketItem(name: String) {
        val ps = connection.prepareStatement("update market_item set count=count-1 where name=?")
        ps.setString(1, name)
        ps.executeQuery()
    }

    /**
     * 유저의 창고에 아이템을 추가합니다
     */
    fun addUserItem(id: Long, userItem: MarketItem) : Boolean {
        return try {
            val ps = connection.prepareStatement("insert into user_item values (?,?,?) on duplicate key update count=count+1")
            ps.setString(1, userItem.name)
            ps.setInt(2, 1)
            ps.setTimestamp(3, Timestamp(Date().time))
            ps.executeQuery()
            true
        }
        catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun add(id: Long, table: String) = connection.createStatement().execute("insert into $table (id) values ($id)")
    private fun del(id: Long, table: String) = connection.createStatement().execute("delete from $table where id = $id")
    private fun check(id: Long, table: String): Boolean {
        val t = connection.createStatement().executeQuery("select count(*) from $table where id = $id")
        return t.next() && t.getInt(1) >= 1
    }
}


