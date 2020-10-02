package com.github.yeoj34760.spuppybot.db

import com.github.yeoj34760.spuppybot.settings
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Database.Companion.connect

object DB {
//    val rpgDB = Database.connect(url = "jdbc:mariadb://172.30.1.7:3306/",
//            user = settings.spuppydb.user,
//            password = settings.spuppydb.password,
//    driver = "com.mysql.jdbc.Driver")


    val spuppyDB =
            connect(url = settings.db.url,
                    user = settings.db.user,
                    password = settings.db.password,
                    driver = "org.mariadb.jdbc.Driver")

    val rpgDB = connect(url = settings.db.rpgUrl,
            user = settings.db.user,
            password = settings.db.password,
            driver = "org.mariadb.jdbc.Driver")


//    private var connection =
//            DriverManager.getConnection(
//                    settings.spuppydb.url,
//                    settings.spuppydb.user,
//                    settings.spuppydb.password)
//
//    fun Connection(): Connection {
//        if (connection.isClosed)
//            connection =
//                    DriverManager.getConnection(
//                            settings.spuppydb.url,
//                            settings.spuppydb.user,
//                            settings.spuppydb.password)
//
//        return connection
//    }
}