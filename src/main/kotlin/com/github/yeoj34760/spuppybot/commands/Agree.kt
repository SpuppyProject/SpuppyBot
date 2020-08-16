package com.github.yeoj34760.spuppybot.commands

import com.github.yeoj34760.spuppybot.sql.SpuppyDBController
import com.github.yeoj34760.spuppybot.waiter
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.util.concurrent.TimeUnit


object Agree : Command() {
    init {
        name = "agree"
        aliases = arrayOf("동의", "가입")
    }

    override fun execute(event: CommandEvent) {
        event.reply("test")
        waiter.waitForEvent(MessageReceivedEvent::class.java,
                { e ->
                    e.author == event.author
                            && e.channel == event.channel
                            && e.message != event.message
                },

                { action ->
                    if (action.message.contentDisplay == "동의") {
                        SpuppyDBController.addUser(event.author.idLong)
                        event.reply("처리 됨")
                    }

                }, 1, TimeUnit.MINUTES) { }
    }
}