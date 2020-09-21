package com.github.yeoj34760.spuppybot.commands.game.market

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.other.DiscordColor
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController
import net.dv8tion.jda.api.EmbedBuilder

@CommandSettings(name = "market", aliases = ["마켓"])
object Market : Command() {
    override fun execute(event: CommandEvent) {
        val items = SpuppyDBController.marketItemList()
        val temp: StringBuffer = StringBuffer()
        val description: String

        items.forEach { item ->
            temp.append("`${item.name}`\n(${item.price}원)[${item.count}개 남음]\n\n")
        }

        description = temp.toString()


        val embed = EmbedBuilder()
                .setTitle("요상스러운 마트")
                .setColor(DiscordColor.YELLOW)
                .setDescription(description).build()

        event.channel.sendMessage(embed).queue()
    }
}