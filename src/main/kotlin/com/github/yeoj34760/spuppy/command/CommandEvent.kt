package com.github.yeoj34760.spuppy.command

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class CommandEvent(api: JDA, responseNumber: Long, message: Message, prefix: String) : MessageReceivedEvent(api, responseNumber, message) {
    val args: List<String>
    val content: String = message.contentRaw.substring(prefix.length + 1).trim()
    init {
        val temp = mutableListOf<String>()
        "[^ ]+".toRegex().findAll(content).forEach { temp.add(it.value) }
        args = temp
    }
}