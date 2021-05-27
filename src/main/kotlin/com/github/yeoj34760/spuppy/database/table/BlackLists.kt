package com.github.yeoj34760.spuppy.database.table

import com.github.yeoj34760.spuppy.database.DBManager
import org.jetbrains.exposed.sql.Table

object BlackLists : Table() {
    val id = long("id")
    val reason = text("reason")
}