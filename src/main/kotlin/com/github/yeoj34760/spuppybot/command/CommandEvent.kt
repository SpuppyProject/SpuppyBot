package com.github.yeoj34760.spuppybot.command

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class CommandEvent(api: JDA, responseNumber: Long, message: Message) : MessageReceivedEvent(api, responseNumber, message) {
    fun args(): List<String> {
        val temp = mutableListOf<String>()
        "(\".+?\"|[^ ]+)".toRegex().findAll(super.getMessage().contentRaw).iterator().forEach { temp.add(it.value) }
        return temp
    }
    fun message() {

    }
}