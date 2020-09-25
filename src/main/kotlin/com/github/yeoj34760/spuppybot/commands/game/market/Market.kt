package com.github.yeoj34760.spuppybot.commands.game.market

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.other.DiscordColor
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController
import com.github.yeoj34760.spuppybot.sql.spuppydb.MarketItemDBController
import net.dv8tion.jda.api.EmbedBuilder
import java.util.logging.Logger
import kotlin.math.ceil

@CommandSettings(name = "market")
object Market : Command() {
    override fun execute(event: CommandEvent) {
        val items = MarketItemDBController.marketItemList()
        val temp = StringBuffer()
        val description: String

        val num = if (event.args.isEmpty()) 1 else event.args[0].toIntOrNull() ?: 1


        if (num > ceil(items.size / 5f))
        {
            event.channel.sendMessage("숫자를 ${ceil(items.size / 5f).toInt()} 이하로 지정해주세요!").queue()
            return
        }
        else if (num <= 0) {
            event.channel.sendMessage("1이상 지정해주세요!").queue()
            return
        }

        for (i in ((num-1)*5) until ((num)*5)) {
            if (items.size <= i)
                break
            temp.append("${i+1}. `${items[i].name}`\n")
            temp.append("(${items[i].price}원)")
            if (items[i].count <= 0)
                temp.append("**[재고 소진]**\n\n")
            else
                temp.append("[${items[i].count}개 남음]\n\n")
        }
        description = temp.toString()


        val embed = EmbedBuilder()
                .setTitle("요상스러운 마트")
                .setColor(DiscordColor.YELLOW)
                .setDescription(description).build()

        event.channel.sendMessage(embed).queue()
    }
}