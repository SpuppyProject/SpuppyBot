package com.github.yeoj34760.spuppybot.commands.game.money

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.DiscordColor
import com.github.yeoj34760.spuppybot.db.UserMoneyDBController
import net.dv8tion.jda.api.EmbedBuilder

@CommandSettings(name = "money")
object Money : Command() {
    override fun execute(event: CommandEvent) {
        val embed = EmbedBuilder().setColor(DiscordColor.GREEN)
                .setTitle("현재 돈")
                .setDescription("${UserMoneyDBController.propertyUser(event.author.idLong)}원")
                .setFooter(event.author.asTag, event.author.avatarUrl ?: event.author.defaultAvatarUrl)
                .build()

        event.channel.sendMessage(embed).queue()
    }
}