package com.github.yeoj34760.spuppy.database.table

import org.jetbrains.exposed.sql.Table

object Users : Table() {
        val id = long("id")
        val money = text("money")
}