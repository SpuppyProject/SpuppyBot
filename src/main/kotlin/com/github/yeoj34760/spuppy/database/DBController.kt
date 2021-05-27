package com.github.yeoj34760.spuppy.database

import com.github.yeoj34760.spuppy.bot.Bot
import com.github.yeoj34760.spuppy.database.table.BlackLists
import com.github.yeoj34760.spuppy.database.table.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction

object DBController {
    private var connect: Database?
    private var able: Boolean = true

    init {
        try {
            connect = Database.connect(
                    Bot.info.spuppyDB.url,
                    driver = "oracle.jdbc.driver.OracleDriver",
                    user = Bot.info.spuppyDB.user,
                    password = Bot.info.spuppyDB.password
            )

            transaction {
                if (!BlackLists.exists())
                    SchemaUtils.create(BlackLists)

                if (!Users.exists())
                    SchemaUtils.create(Users)
            }
        } catch (e: Exception) {
            connect = null
            able = false
        }
    }
}