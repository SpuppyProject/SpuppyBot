package com.github.yeoj34760.spuppybot.sql.spuppydb

import com.github.yeoj34760.spuppybot.other.Util
import com.github.yeoj34760.spuppybot.spuppyDBConnection
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController
import com.github.yeoj34760.spuppybot.sql.userbox.UserBox

object UserBoxDBController {
    fun addUserBox(id: Long, track: String) {
   val ps = spuppyDBConnection.prepareStatement("insert into user_box values (?,?,?)")
        ps.setLong(1, id)
        ps.setString(2, track)
        ps.setInt(3, fromMaxNumber(id)+1)
        ps.execute()
    }
    fun delAllUserBox(id: Long) {
        val ps = spuppyDBConnection.prepareStatement("delete from user_box where id=?")
        ps.setLong(1, id)
        ps.execute()
    }

    fun delUserBox(id: Long, order: Int) {
        //제거
        delUserBoxStatement("delete from user_box where id = ? and `order` = ?", id , order)
        //재정렬
        delUserBoxStatement("update user_box set `order` = `order` - 1 where id = ? and `order` > ?", id , order)
    }

    private fun delUserBoxStatement(sql: String, id: Long, order: Int) {
        val pstat = spuppyDBConnection.prepareStatement(sql)
        pstat.setLong(1, id)
        pstat.setInt(2, order)
        pstat.executeQuery()
    }

    fun checkUserBox(id: Long): Boolean {
        val ps = spuppyDBConnection.prepareStatement("select exists(select * from user_box where ?)")
        ps.setLong(1, id)
        val result = ps.executeQuery()
        if (result.next())
            return result.getInt(1) == 1

        return false
    }

    fun fromUserBox(id: Long): List<UserBox> {
        val tempBox = arrayListOf<UserBox>()
        val t = spuppyDBConnection.createStatement().executeQuery("select track, `order` from user_box where id = $id order by `order`")
        while (t.next()) {
            val order: Int = t.getString(2).toInt()
            tempBox.add(UserBox(id, Util.base64ToTrack(t.getString(1)), order))
        }

        return tempBox
    }

    /**
     * 해당 유저박스에서 몇 개 있는지 반환합니다.
     * 찾을 수 없을 경우 0로 반환합니다.
     */
    fun fromMaxNumber(id: Long): Int {
        val t = SpuppyDBController.connection.createStatement().executeQuery("select max(`order`) from user_box where id = $id")
        if (t.next()) return t.getInt(1)
        return 0
    }

    /**
     * 순서를 서로 바꿉니다.
     */
    fun moveBox(id: Long, num1: Int, num2: Int) {
        val statement = SpuppyDBController.connection.createStatement()
        //dog same
        statement.execute("update user_box a inner join user_box b on a.`order` <> b.`order` set a.`order` = b.`order` where a.`order` in ($num1,$num2) and b.`order` in ($num1,$num2)")
    }
}
