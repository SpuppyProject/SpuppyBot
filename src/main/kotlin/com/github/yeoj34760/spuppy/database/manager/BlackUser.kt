package com.github.yeoj34760.spuppy.database.manager

import com.github.yeoj34760.spuppy.database.table.BlackLists
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

data class BlackUser(val id: Long, val reason: String) {
    var removed: Boolean = false
        private set

    fun remove() {
        val _id = id
        transaction {
            BlackLists.deleteWhere { BlackLists.id eq _id }
        }
        removed = true
    }

    companion object {
        fun add(id: Long, reason: String) {
            transaction {
                BlackLists.insert {
                    it[BlackLists.id] = id
                    it[BlackLists.reason] = reason
                }
            }
        }
    }
}