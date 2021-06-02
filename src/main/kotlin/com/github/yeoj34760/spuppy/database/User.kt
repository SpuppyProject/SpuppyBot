package com.github.yeoj34760.spuppy.database

import com.github.yeoj34760.spuppy.database.table.Users
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.math.BigDecimal

data class User(val id: Long, private val _money: BigDecimal) {
    var usedTime: Long = System.currentTimeMillis()
        private set

    var money: BigDecimal = _money
        set(value) {
            DBController.cache.updateUserEvent(this)
            usedTime = System.currentTimeMillis()
            field = value
        }
        get() {
            usedTime = System.currentTimeMillis()
            return field
        }
}
