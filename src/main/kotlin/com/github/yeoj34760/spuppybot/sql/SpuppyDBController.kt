package com.github.yeoj34760.spuppybot.sql

import com.github.yeoj34760.spuppybot.Settings
import java.sql.Connection
import java.sql.DriverManager

object SpuppyDBController {
   private val connection =
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

    fun addUserBox(id: Long) = add(id, "user_box")
    fun delUserBox(id: Long) = del(id, "user_box")
    fun checkUserBox(id: Long): Boolean = check(id, "user_box")
    fun fromUserBox(id: Long): UserBox {
        val tempURL = arrayListOf<String>()
        val tempName = arrayListOf<String>()
        val t = connection.createStatement().executeQuery("select * from user_box where id = $id")
        while (t.next()) {
            tempURL.add(t.getString(UserBoxSql.URL.ordinal))
            tempName.add(t.getString(UserBoxSql.NAME.ordinal))
        }

        return UserBox(tempURL, tempName)
    }

    fun addUser(id: Long) = add(id, "user")
    fun delUser(id: Long) = del(id, "user")
    fun checkUser(id: Long): Boolean = check(id, "user")


    private fun add(id: Long, table: String) = connection.createStatement().execute("insert into $table (id) values ($id)")
    private fun del(id: Long, table: String) = connection.createStatement().execute("delete from $table where id = $id")
    private fun check(id: Long, table: String): Boolean {
        val t = connection.createStatement().executeQuery("select count(*) from $table where id = $id")
        return t.next() && t.getInt(1) >= 1
    }
}