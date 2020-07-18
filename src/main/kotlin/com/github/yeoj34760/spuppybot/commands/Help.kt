package com.github.yeoj34760.spuppybot.commands

import com.github.yeoj34760.spuppybot.other.DiscordColor
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.EmbedBuilder

object Help : Command() {
    init {
        name = "help"
        aliases = arrayOf("help", "헬프", "도움말","ㅚ데")
    }
    override fun execute(event: CommandEvent) {
        val embed = EmbedBuilder()
                .setColor(DiscordColor.YELLOW)
                .setDescription("[페이지](https://spuppyproject.github.io/SpuppyBot/help/)에서 확인해주세요.")
                .setAuthor(event.author.name, null, event.author.avatarUrl)
    }

}