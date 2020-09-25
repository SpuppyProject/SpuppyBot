package com.github.yeoj34760.spuppybot.item

import java.sql.Timestamp

data class UserItem(
        val name: String,
        val count: Int,
        val timestamp: Timestamp
)