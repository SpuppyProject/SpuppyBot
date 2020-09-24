package com.github.yeoj34760.spuppybot

import com.google.gson.annotations.SerializedName

data class Settings(val token: String,
                    val prefix: String,
                    @SerializedName("owner_id")
                    val ownerId: String,
                    val version: String,
                    val spuppydb: SpuppyDB)
data class SpuppyDB(val url: String, val user: String, val password: String)
