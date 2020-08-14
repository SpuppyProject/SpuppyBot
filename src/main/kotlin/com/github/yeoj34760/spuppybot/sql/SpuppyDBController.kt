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
    fun addGuild(id: Long) = connection.createStatement().execute("insert into guild (id) values ($id);")
    fun delGuild(id: Long) = connection.createStatement().execute("delete from guild where id = $id")
    fun checkGuild(id: Long): Boolean {
        val t = connection.createStatement().executeQuery("select count(*) from guild where id = $id")
        return t.next() && t.getInt(1) >= 1
    }
}