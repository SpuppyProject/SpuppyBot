package com.github.yeoj34760.spuppybot.sql

import com.github.yeoj34760.spuppybot.Settings
import java.sql.DriverManager

object SpuppyDBController {
    var connection =
            DriverManager.getConnection(
                    Settings.SPUPPYDB_URL,
            Settings.SPUPPYDB_USER,
            Settings.SPUPPYDB_PASSWORD)

    fun guildVolume(id: Long): Int {
        val t = connection.createStatement().executeQuery("select volume from guild where id = $id")
        if (t.next())
            return t.getInt(1)
        return -1
    }
    fun guildVolume(id: Long, volume: Int) {
        connection.createStatement().execute("update guild set volume = $volume where id = $id")
    }


    fun addGuild(id: Long) = connection.createStatement().execute("insert into guild (id) values ($id);")

}