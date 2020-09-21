package com.github.yeoj34760.spuppybot.commands.game.money

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.other.DiscordColor
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController
import net.dv8tion.jda.api.EmbedBuilder

@CommandSettings(name="money")
object Money : Command() {
    override fun execute(event: CommandEvent) {
        val embed = EmbedBuilder().setColor(DiscordColor.GREEN)
                .setAuthor(event.author.asTag, null, event.author.avatarUrl ?: event.author.defaultAvatarUrl)
                .setTitle("현재 돈")
                .setDescription("${SpuppyDBController.propertyUser(event.author.idLong).toString()}원")
                .build()

        event.channel.sendMessage(embed).queue()
    }
}