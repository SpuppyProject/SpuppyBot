package com.github.yeoj34760.spuppy.utilities.database

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

object DatabaseUsers : LongIdTable("user") {
    val money: Column<String> = text("money")
    val customCoin: Column<String?> = text("custom_coin").nullable()
}


class DatabaseUser(id: EntityID<Long>): LongEntity(id) {
    companion object : LongEntityClass<DatabaseUser>(DatabaseUsers)
    var money by DatabaseUsers.money
    var customCoin by DatabaseUsers.customCoin
}