package com.github.yeoj34760.spuppy.utilities.language

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

typealias Sentence = MutableMap<Languages, MutableMap<String, String>>

object Language {
    private val sentence: Sentence = mutableMapOf()
    private val users: MutableMap<Long, Languages> = mutableMapOf()
    val defaultLanguage = Languages.KOREAN

    init {
        Languages.values().forEach {
            sentence[it] = mutableMapOf()
            val element =
                Json.parseToJsonElement(javaClass.getResource("/languages/${it.name.toLowerCase()}.json").readText())
            element.jsonObject.forEach { id, content -> sentence[it]!![id] = content.jsonPrimitive.content }
        }
    }

    fun toText(sentenceId: String, userId: Long): String {
        if (users.containsKey(userId))
            return sentence[users[userId]!!]!![sentenceId]!!
        return sentence[defaultLanguage]!![sentenceId]!!
    }
}
