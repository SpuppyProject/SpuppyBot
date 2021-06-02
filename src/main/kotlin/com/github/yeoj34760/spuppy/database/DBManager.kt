package com.github.yeoj34760.spuppy.database

import com.github.yeoj34760.spuppy.database.manager.BlackUser
import com.github.yeoj34760.spuppy.database.table.BlackLists
import com.github.yeoj34760.spuppy.database.table.Users
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

class DBManager {
//    val blackList: MutableList<BlackUser> = mutableListOf()
//
//    init {
//        transaction {
//            BlackLists.selectAll().forEach {
//                blackList.add(BlackUser(it[BlackLists.id], it[BlackLists.reason]))
//            }
//        }
//    }


    fun loadUser(id: Long): User? {
        return transaction {
            val money = Users.select { Users.id eq id }.firstOrNull()?.get(Users.money)

            if (money != null)
                User(id, BigDecimal(money))

            null
        }
    }

    fun addUser(user: User) {
        transaction {
            Users.insert { it[Users.id] = user.id; it[Users.money] = user.money.toString() }
        }
    }
}