package com.github.yeoj34760.spuppybot.db

import com.google.gson.Gson
import com.google.gson.JsonObject
import java.util.*

object Base {
    fun decode(str: String): String = String(Base64.getDecoder().decode(str))
    fun encode(str: String): String = Base64.getEncoder().encodeToString(str.toByteArray())
    fun encode(json: JsonObject): String = Base64.getEncoder().encodeToString(Gson().toJson(json).toByteArray())
}