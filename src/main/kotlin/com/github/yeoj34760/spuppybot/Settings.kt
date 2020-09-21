package com.github.yeoj34760.spuppybot

data class Settings(val token: String,
                    val prefix: String,
                    val ownerId: String,
                    val version: String,
                    val spuppydb: SpuppyDB)
data class SpuppyDB(val url: String, val user: String, val password: String)
