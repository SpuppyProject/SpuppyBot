package com.github.yeoj34760.spuppy.command

import com.github.yeoj34760.spuppy.database.DBController
import com.github.yeoj34760.spuppy.utilities.enhance.KEmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.util.function.Consumer

/**
 * Custom MessageReceivedEvent
 */
class CommandEvent(api: JDA, responseNumber: Long, message: Message, prefix: String) :
    MessageReceivedEvent(api, responseNumber, message) {
    val args: List<String>
    val content: String = message.contentRaw.substring(prefix.length + 1).trim()
    val db = DBController
    init {
        val temp = mutableListOf<String>()
        "[^ ]+".toRegex().findAll(content).forEach { temp.add(it.value) }
        args = temp
    }

    fun send(text: CharSequence): Message = channel.sendMessage(text).complete()
    fun send(msg: Message): Message = channel.sendMessage(msg).complete()
    fun send(embed: MessageEmbed): Message = channel.sendMessage(embed).complete()
    fun send(embed: KEmbedBuilder.() -> Unit): Message = channel.sendMessage(KEmbedBuilder().apply(embed).build()).complete()


    fun send(text: CharSequence, success: Consumer<Message>) = channel.sendMessage(text).queue(success)
    fun send(msg: Message, success: Consumer<Message>) = channel.sendMessage(msg).queue(success)
    fun send(embed: MessageEmbed, success: Consumer<Message>) = channel.sendMessage(embed).queue(success)
    fun ping(): Long = jda.gatewayPing
}