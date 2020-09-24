package com.github.yeoj34760.spuppybot.commands.game.market

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.item.MarketItem
import com.github.yeoj34760.spuppybot.market.MarketItemList
import com.github.yeoj34760.spuppybot.sql.spuppydb.MarketItemDBController
import com.github.yeoj34760.spuppybot.sql.spuppydb.UserItemDBController
import com.github.yeoj34760.spuppybot.sql.spuppydb.UserMoneyDBController
import java.math.BigInteger


@CommandSettings(name = "buymarket", aliases = ["마켓 구매"])
object BuyMarket : Command() {
    override fun execute(event: CommandEvent) {
//        val itemList = MarketItemDBController.marketItemList()

        var item: MarketItem? = MarketItemList[event.content]

        if (item == null) {
            event.channel.sendMessage("해당 아이템이 상점에 없습니다.").queue()
            return
        }

//        val item = itemList.stream().filter { it.name == event.content }.findFirst().get()

        if (UserMoneyDBController.propertyUser(event.author.idLong).compareTo(BigInteger(item.price.toString())) == -1) {
            event.channel.sendMessage("현재 돈으로 해당 아이템을 살 수가 없어요!").queue()
            return
        }

        if(UserItemDBController.addUserItem(event.author.idLong, item)) {
            UserMoneyDBController.minusMoneyUser(event.author.idLong, BigInteger(item.price.toString()))
            event.channel.sendMessage("구매완료!").queue()
            --item
        }
        else
            event.channel.sendMessage("처리하는데에 오류가 발생했어요!").queue()

    }
}