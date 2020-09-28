package com.github.yeoj34760.spuppybot.db

import com.github.yeoj34760.spuppybot.settings
import java.sql.Connection
import java.sql.DriverManager

object DB {
    private var connection =
            DriverManager.getConnection(
                    settings.spuppydb.url,
                    settings.spuppydb.user,
                    settings.spuppydb.password)

    fun Connection(): Connection {
        if (connection.isClosed)
            connection =
                    DriverManager.getConnection(
                            settings.spuppydb.url,
                            settings.spuppydb.user,
                            settings.spuppydb.password)

        return connection
    }
}