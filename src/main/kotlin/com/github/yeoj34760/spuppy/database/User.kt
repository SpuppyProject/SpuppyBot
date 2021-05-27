package com.github.yeoj34760.spuppy.database

import com.github.yeoj34760.spuppy.database.table.Users
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.math.BigDecimal

data class User(val id: Long, val _money: BigDecimal) {
    var money: BigDecimal = _money
        set(value) {
            val _id = id
            transaction {
                Users.update({ Users.id eq _id }) {
                    it[money] = value.toString()
                }
            }

            field = value
        }

    companion object {
        fun create(id: Long): User {
            DBManager //call DBManager init function
            return transaction {
                val user: ResultRow? = Users.select { Users.id eq id }.firstOrNull()

                var money: String = if (user == null) {
                    Users.insert { it[Users.id] = id; it[money] = "0" };"0"
                } else {
                    user[Users.money]
                }
                User(id, BigDecimal(money))
            }
        }
    }
}
