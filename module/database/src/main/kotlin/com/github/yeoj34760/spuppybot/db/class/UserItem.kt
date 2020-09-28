package com.github.yeoj34760.spuppybot.db.`class`

import java.sql.Timestamp

data class UserItem(
        val name: String,
        val count: Int,
        val timestamp: Timestamp
)