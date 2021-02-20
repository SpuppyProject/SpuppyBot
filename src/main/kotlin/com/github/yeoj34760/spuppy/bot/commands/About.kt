package com.github.yeoj34760.spuppy.bot.commands

import com.github.yeoj34760.spuppy.bot.Bot
import com.github.yeoj34760.spuppy.utilities.enhance.EmbedColor
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent

object About: Command(name = "about", aliases = Bot.commands["help"] ?: error("umm..")) {
    override suspend fun execute(event: CommandEvent) {
        event.send {
            author {
                name = "about Spuppybot"
            }

            color = EmbedColor.BLUE.rgb

            description = """
                스퍼피봇은 단순 음악, 도박등등 기능을 가진 봇입니다.
            """.trimIndent()
            addField {
                name = "using libraries"
                value = """
                    [JDA](https://github.com/DV8FromTheWorld/JDA)
                    [LavaPlayer](https://github.com/sedmelluq/lavaplayer)
                """.trimIndent()
                inline = true
            }

            addField {
                name = "만든이"
                value = """
                    ${event.jda.retrieveUserById(Bot.info.ownerId).complete().asTag}
                """.trimIndent()
                inline = true
            }

            addField {
                name = "servers"
                value = event.jda.guilds.size.toString()
                inline = true
            }
        }
    }
}