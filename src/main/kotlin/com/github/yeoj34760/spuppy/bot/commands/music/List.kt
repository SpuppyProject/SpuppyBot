package com.github.yeoj34760.spuppy.bot.commands.music

import com.github.yeoj34760.spuppy.bot.Bot
import com.github.yeoj34760.spuppy.bot.player.PlayerGuildManager
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import kotlin.math.ceil

object List : Command(name = "list", aliases = Bot.commands["list"] ?: error("umm..")) {
    private const val pageMax = 10

    override suspend fun execute(event: CommandEvent) {
        if (PlayerGuildManager[event.guild] != null
                && !PlayerGuildManager[event.guild]!!.isPlayed()
                || PlayerGuildManager[event.guild]!!.trackList().isEmpty()) {
            event.send("리스트에 텅 비어있어요")
            return
        }



        if (event.args.isNotEmpty() && event.args[0].toIntOrNull() == null) {
            event.send("명령어를 잘못 쓰셨어요!")
            return
        }
        val control = PlayerGuildManager[event.guild]!!
        val pagePosition = if (event.args.isEmpty()) 0 else event.args[0].toInt() - 1

        val pageCount = ceil((control.trackList().count().toDouble() / pageMax)).toInt()
        val contentEmbed = StringBuffer()

        if (pagePosition >= pageCount || pagePosition < 0) {
            event.send("숫자가 뭔가 잘못되었어요")
            return
        }

        for (i in pageMax * pagePosition until pageMax * pagePosition + pageMax) {
            if (i >= control.trackList().count())
                continue
            contentEmbed.append("${i + 1}. ${control.trackList()[i].info.title}\n")
        }

        event.send() {
            description = contentEmbed.toString()
            author { name = "${event.guild.name}'s playlist"; iconUrl = event.author.avatarUrl ?: event.author.defaultAvatarUrl}
            footer { text = "${pagePosition+1}/${pageCount}" }
        }
    }
}