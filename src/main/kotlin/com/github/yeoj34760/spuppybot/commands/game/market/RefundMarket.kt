package com.github.yeoj34760.spuppybot.commands.game.market

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.market.MarketItemList
import com.github.yeoj34760.spuppybot.sql.spuppydb.UserItemDBController
import com.github.yeoj34760.spuppybot.sql.spuppydb.UserMoneyDBController
import java.math.BigDecimal

@CommandSettings(name = "refundmarket", aliases = ["환불"])
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
            event.channel.sendMessage("해당 아이템을 가지고 있지 않아요!").queue()
            return
        }
        var money: BigDecimal = BigDecimal.ZERO
        for (i in 1..count) {
            item++
            UserItemDBController.minusUserItem(event.author.idLong, item)
            money = money.add(BigDecimal(item.price.toString()))
        }
        money = money.multiply(BigDecimal("0.7"))

        UserMoneyDBController.addMoneyUser(event.author.idLong, money.toBigInteger())

        event.channel.sendMessage("팔았어유").queue()

    }
}