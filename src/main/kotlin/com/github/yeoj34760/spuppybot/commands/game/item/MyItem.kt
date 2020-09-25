package com.github.yeoj34760.spuppybot.commands.game.item

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.other.DiscordColor
import com.github.yeoj34760.spuppybot.sql.spuppydb.MarketItemDBController
import com.github.yeoj34760.spuppybot.sql.spuppydb.UserItemDBController
import net.dv8tion.jda.api.EmbedBuilder
import java.math.BigInteger
import java.text.SimpleDateFormat
import java.util.*

@CommandSettings(name = "myitem")
object MyItem : Command() {
    override fun execute(event: CommandEvent) {
        val userItemList = UserItemDBController.fromUserItemList(event.author.idLong)
        if (userItemList.isEmpty()) {
            event.channel.sendMessage("창고에 열심히 뒤져봤지만 아무 것도 없네요").queue()
            return
        }
        val tempString: StringBuffer = StringBuffer()
        var sobeMoney: BigInteger = BigInteger.ZERO
        val marketList = MarketItemDBController.marketItemList()
        userItemList.forEach { userItem ->
            tempString.append("**${userItem.name}**\n")
            if (userItem.count > 1)
                tempString.append("갯수: `${userItem.count}개`\n")
            val format = SimpleDateFormat("yyyy-MM-dd")
            tempString.append("마지막으로 산 날짜: `${format.format(Date(userItem.timestamp.time).time)}`\n\n")
            sobeMoney = sobeMoney.add(BigInteger("${marketList.stream().filter { it.name == userItem.name }.findFirst().get().price * userItem.count}"))
        }

        val embed = EmbedBuilder().setColor(DiscordColor.ORANGE)
                .setTitle("내 아이템들")
                .setDescription(tempString)
                .addField("아이템 수", "${userItemList.size}", true)
                .addField("소비한 돈", "${sobeMoney}", true)
                .build()

        event.channel.sendMessage(embed).queue()

    }
}