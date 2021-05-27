package com.github.yeoj34760.spuppy.database

import com.github.yeoj34760.spuppy.database.manager.BlackUser
import com.github.yeoj34760.spuppy.database.table.BlackLists
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

internal object DBManager {
    val blackList: MutableList<BlackUser> = mutableListOf()

    init {
        transaction {
            BlackLists.selectAll().forEach {
                blackList.add(BlackUser(it[BlackLists.id], it[BlackLists.reason]))
            }
        }
    }
}