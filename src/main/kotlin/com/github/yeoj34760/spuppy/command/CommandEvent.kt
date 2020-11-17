package com.github.yeoj34760.spuppy.command

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class CommandEvent(api: JDA, responseNumber: Long, message: Message) : MessageReceivedEvent(api, responseNumber, message) {}