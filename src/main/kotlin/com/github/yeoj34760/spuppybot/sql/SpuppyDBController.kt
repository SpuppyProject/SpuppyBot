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


    /**
     * 특정 길드 볼륨 값을 불러옵니다.
     */
    fun guildVolume(id: Long): Int {
        val t = connection.createStatement().executeQuery("select volume from guild where id = $id")
        if (t.next())
            return t.getInt(1)
        return -1
    }

    /**
     * 특정 길드 볼륨 설정을 설정합니다.
     */
    fun guildVolume(id: Long, volume: Int) = connection.createStatement().execute("update guild set volume = $volume where id = $id")

    /**
     * 데이터베이스에 특정 길드를 추가합니다.
     */
    fun addGuild(id: Long) = add(id, "guild")

    /**
     * 데이터베이스에 있는 길드 정보를 삭제합니다.
     */
    fun delGuild(id: Long) = del(id, "guild")

    /**
     * 데이터베이스에 길드 정보가 있는지 확인합니다.
     * @return 있을 경우 true, 없을 경우 false
     * */
    fun checkGuild(id: Long): Boolean = check(id, "guild")

    fun addUserBox(id: Long, track: String) = connection.createStatement().execute("insert into user_box values ($id, '$track', ${fromMaxNumber(id) + 1})")
    fun delAllUserBox(id: Long) = del(id, "user_box")
    fun delUserBox(id: Long, order: Int) {
        //제거
        delUserBoxStatement("delete from user_box where id = ? and `order` = ?", id , order)
        //재정렬
        delUserBoxStatement("update user_box set `order` = `order` - 1 where id = ? and `order` > ?", id , order)
    }

    private fun delUserBoxStatement(sql: String, id: Long, order: Int) {
        val pstat = connection.prepareStatement(sql)
        pstat.setLong(1, id)
        pstat.setInt(2, order)
        pstat.executeQuery()
    }

    fun checkUserBox(id: Long): Boolean = check(id, "user_box")
    fun fromUserBox(id: Long): List<UserBox> {
        val tempBox = arrayListOf<UserBox>()
        val t = connection.createStatement().executeQuery("select track, `order` from user_box where id = $id order by `order`")
        while (t.next()) {
            val order: Int = t.getString(2).toInt()
            tempBox.add(UserBox(id, Util.base64ToTrack(t.getString(1)), order))
        }

        return tempBox
    }

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

    /**
     * 순서를 서로 바꿉니다.
     */
    fun moveBox(id: Long, num1: Int, num2: Int) {
        val statement = connection.createStatement()
        //dog same
        statement.execute("update user_box a inner join user_box b on a.`order` <> b.`order` set a.`order` = b.`order` where a.`order` in ($num1,$num2) and b.`order` in ($num1,$num2)")
    }

    /**
     * 해당 유저박스에서 몇 개 있는지 반환합니다.
     * 찾을 수 없을 경우 0로 반환합니다.
     */
    fun fromMaxNumber(id: Long): Int {
        val t = connection.createStatement().executeQuery("select max(`order`) from user_box where id = $id")
        if (t.next()) return t.getInt(1)
        return 0
    }

    fun addUser(id: Long) = add(id, "user")
    fun delUser(id: Long) = del(id, "user")
    fun checkUser(id: Long): Boolean = check(id, "user")



    fun addMoneyUser(id: Long) = add(id, "user_money")
    fun delMoneyUser(id: Long) = del(id, "user_money")
    fun checkMoneyUser(id: Long) = check(id, "user_money")
    fun propertyUser(id: Long) : BigInteger {
        val t = connection.createStatement().executeQuery("select money from user_money where id = $id")
        if (t.next()) {
            return BigInteger(t.getString(1))
        }

        throw Exception("오류 내용: 재산을 확인할 수 없음")

    }

    fun receiveMoneyUser(id: Long, money: BigInteger) : BigInteger {
        val oldMoney = propertyUser(id)
        val newMoney = oldMoney.add(money)
        connection.createStatement().execute("update user_money set money=${newMoney} where id=$id")

        return newMoney
    }


    fun receiveMoneyTimer(id: Long): Long {
        val t = connection.createStatement().executeQuery("select timer from user_receive_money_timer where id = $id")
        if (t.next()) {
            return t.getTimestamp(1).time
        }

        throw Exception("돈받기 타이머 값 가져오기 실패함")
    }
    fun setupReceiveMoneyTimer(id: Long) {
        val pr = connection.prepareStatement("update user_receive_money_timer set timer=? where id=?")
        pr.setLong(2, id)
        pr.setTimestamp(1, Timestamp(Date().time + 60000))
        pr.executeQuery()
    }

    fun createReceiveMoneyUser(id: Long) {
        val pr = connection.prepareStatement("insert into user_receive_money_timer (id, timer) values (?, ?)")
        pr.setLong(1, id)
        pr.setTimestamp(2, Timestamp(Date().time))
        pr.executeQuery()
    }

    fun checkReceiveMoneyUser(id: Long) = check(id, "user_receive_money_timer")


    fun minusReceiveMoneyUser(id: Long, money: BigInteger) {
        val nowMoney = propertyUser(id)
            val ps = connection.prepareStatement("update user_money set money=? where id=?")
        nowMoney.minus(money).toString()
        ps.setString(1, nowMoney.minus(money).toString())
        ps.setLong(2, id)
                ps.executeQuery()
    }
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

    fun minusMarketItem(name: String) {
        val ps = connection.prepareStatement("update market_item set count=count-1 where name=?")
        ps.setString(1, name)
        ps.executeQuery()
    }

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


