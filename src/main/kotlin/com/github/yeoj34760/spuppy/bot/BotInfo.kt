package com.github.yeoj34760.spuppy.bot

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BotInfo(
    val token: String,
    val prefix: String,
    @SerialName("owner_id") val ownerId: Long,
    val version: String,
    val spuppyDB: SpuppyDB
)

@Serializable
data class SpuppyDB(
    val url: String,
    val user: String,
    val password: String
)
