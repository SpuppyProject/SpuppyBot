package com.github.yeoj34760.spuppybot.commands.game.market

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.market.MarketItemList
import com.github.yeoj34760.spuppybot.other.DiscordColor
import com.github.yeoj34760.spuppybot.sql.spuppydb.UserItemDBController
import com.github.yeoj34760.spuppybot.sql.spuppydb.UserMoneyDBController
import net.dv8tion.jda.api.EmbedBuilder
import java.math.BigDecimal

@CommandSettings(name = "refundmarket")
object RefundMarket : Command() {
    override fun execute(event: CommandEvent) {
        if (event.args.isEmpty()) {
            event.channel.sendMessage("아이템 이름을 적어주세요!").queue()
        }
        val regex = "(?<= )[0-9]+".toRegex()
        val temp = event.content.replace(regex, "").trim()
        val countFind = regex.find(event.content)

        val count = countFind?.value?.toInt() ?: 1
        var item = MarketItemList[temp]

        if (item == null) {
            event.channel.sendMessage("해당 이름을 가진 아이템을 찾을 수 없어요").queue()
            return
        }
        
        val myItem = UserItemDBController[event.author.idLong, temp]
        
        if (myItem == null) {
            event.channel.sendMessage("해당 아이템을 가지고 있지 않아요!").queue()
            return
        }
        else {
            if (myItem.count < count) {
                event.channel.sendMessage("자신이 가지고 있는 아이템 수보다 많이 팔 수 없어요").queue()
                return
            }
        }
//        for (i in 1..count) {
//            item++
//            UserItemDBController.minusUserItem(event.author.idLong, item)
//            money = money.add(BigDecimal(item.price.toString()))
//        }

        item.add(count)
        UserItemDBController.minusUserItem(event.author.idLong, item, count)
        var money: BigDecimal = BigDecimal((item.price*count).toString())
        money = money.multiply(BigDecimal("0.7"))

        UserMoneyDBController.addMoneyUser(event.author.idLong, money.toBigInteger())

        val embed = EmbedBuilder().setColor(DiscordColor.GREEN)
                .setTitle("반품완료")
                .setDescription("`${item.name}`을(를) 반품했어요")
                .addField("반품한 갯수", count.toString(), true)
                .addField("받은 돈", money.toBigInteger().toString(), true)
                .setFooter("반품하면 원가의 70%정도 돈을 돌려받습니다.")
                .build()

                event.channel.sendMessage(embed).queue()
    }
}