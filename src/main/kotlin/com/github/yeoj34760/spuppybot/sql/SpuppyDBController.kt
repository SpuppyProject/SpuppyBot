package com.github.yeoj34760.spuppybot.sql

import com.github.yeoj34760.spuppybot.Settings
import com.github.yeoj34760.spuppybot.command.CommandInfo
import com.github.yeoj34760.spuppybot.sql.userbox.UserBox
import com.github.yeoj34760.spuppybot.sql.userbox.UserBoxInfo
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.sql.DriverManager
import java.util.*

object SpuppyDBController {
    val connection =
            DriverManager.getConnection(
                    Settings.SPUPPYDB_URL,
                    Settings.SPUPPYDB_USER,
                    Settings.SPUPPYDB_PASSWORD)


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

    fun addUserBox(id: Long, info: String) = connection.createStatement().execute("insert into user_box values ($id, '$info', ${fromMaxNumber(id) + 1})")
    fun delAllUserBox(id: Long) = del(id, "user_box")
    fun delUserBox(id: Long, order: Int) {
        val statement = connection.createStatement()
        statement.execute("delete from user_box where id = $id and `order` = $order")
        //재정렬
        statement.execute("update user_box set `order` = `order` - 1 where id = $id and `order` > $order")
    }

    fun checkUserBox(id: Long): Boolean = check(id, "user_box")
    fun fromUserBox(id: Long): List<UserBox> {
        val tempBox = arrayListOf<UserBox>()
        val t = connection.createStatement().executeQuery("select info, `order` from user_box where id = $id order by `order`")
        while (t.next()) {
            val json = Base64.getDecoder().decode(t.getString(1))
            val info = Json.decodeFromString<UserBoxInfo>(String(json))
            val order: Int = t.getString(2).toInt()
            tempBox.add(UserBox(info, order))
        }

        return tempBox
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


    fun fromCommands(): MutableList<CommandInfo> {
        val temp: MutableList<CommandInfo> = mutableListOf()
        val t = connection.createStatement().executeQuery("select name, command, _group from command")
        while (t.next())
        {
            val tempCommand = CommandInfo(t.getString(1), t.getString(2), t.getString(3))//name, command, group
            temp.add(tempCommand)
        }
        return temp
    }

    private fun add(id: Long, table: String) = connection.createStatement().execute("insert into $table (id) values ($id)")
    private fun del(id: Long, table: String) = connection.createStatement().execute("delete from $table where id = $id")
    private fun check(id: Long, table: String): Boolean {
        val t = connection.createStatement().executeQuery("select count(*) from $table where id = $id")
        return t.next() && t.getInt(1) >= 1
    }
}


