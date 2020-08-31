package com.github.yeoj34760.spuppybot.command

import com.github.yeoj34760.spuppybot.music.GuildManager
import com.github.yeoj34760.spuppybot.music.PlayerControl
import com.github.yeoj34760.spuppybot.other.Util
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class CommandEvent(private val messageReceivedEvent: MessageReceivedEvent, prefix: String) {
    val args: List<String>

    init {
        args = if (messageReceivedEvent.message.contentRaw.count() <= prefix.count())
            listOf()
        else {
            val command = messageReceivedEvent.message.contentRaw.substring(prefix.count())
            Util.stringToArgs(command)
        }
    }


    fun argsToString(): String {
        val temp: StringBuffer = StringBuffer()
        args.forEach { temp.append(it) }
        return temp.toString()
    }

    val playerControl: PlayerControl? = GuildManager.playerControls[messageReceivedEvent.guild.idLong]

    val channel: MessageChannel = messageReceivedEvent.channel


    val messageId: String = messageReceivedEvent.messageId


    val messageIdLong: Long = messageReceivedEvent.messageIdLong


    val isFromGuild: Boolean = messageReceivedEvent.isFromGuild


    val channelType: ChannelType = messageReceivedEvent.channelType


    val guild: Guild = messageReceivedEvent.guild


    val textChannel: TextChannel = messageReceivedEvent.textChannel


    //val privateChannel: PrivateChannel? = messageReceivedEvent.privateChannel

    val message: Message = messageReceivedEvent.message


    val author: User = messageReceivedEvent.author


    val member: Member? = messageReceivedEvent.member


    val webhookMessage: Boolean = messageReceivedEvent.isWebhookMessage

    val jda: JDA = messageReceivedEvent.jda

    val guildIdLong: Long = messageReceivedEvent.guild.idLong
}