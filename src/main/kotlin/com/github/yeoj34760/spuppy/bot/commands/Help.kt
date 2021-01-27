package com.github.yeoj34760.spuppy.bot.commands

import com.github.yeoj34760.spuppy.bot.Bot
import com.github.yeoj34760.spuppy.bot.enhance.EmbedColor
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent

object Help : Command(name = "help", aliases = Bot.commands["help"] ?: error("umm..")) {
    override suspend fun execute(event: CommandEvent) {
        event.send {
            author {
                name = "도움말"
            }

            color = EmbedColor.BLUE.rgb

            addField {
                name = "음악"
                value = """
                    ?play {URI 또는 음악 제목}
                    ?nowplay
                    ?list
                    ?skip
                    ?stop
                """.trimIndent()
            }

            addField {
                name = "도박"
                value = """
                    ?도박 {값}
                """.trimIndent()
            }
        }
    }
}