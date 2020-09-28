package com.github.yeoj34760.spuppybot.commands.game.market

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.db.`class`.MarketItem
import com.github.yeoj34760.spuppybot.market.MarketItemList
import com.github.yeoj34760.spuppybot.db.UserItemDBController
import com.github.yeoj34760.spuppybot.db.UserMoneyDBController
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigInteger


@CommandSettings(name = "buymarket")
object BuyMarket : Command() {
    private val logger: Logger = LoggerFactory.getLogger("마켓 구매")
    override fun execute(event: CommandEvent) {
        var item: MarketItem? = MarketItemList[event.content]

        if (item == null) {
            event.channel.sendMessage("해당 아이템이 상점에 없습니다.").queue()
            return
        }

        if (item.count <= 0) {
            event.channel.sendMessage("해당 아이템은 재고가 없네요.").queue()
            return
        }

        val property = UserMoneyDBController.propertyUser(event.author.idLong)
        val itemPrice = BigInteger(item.price.toString())
        if (property.compareTo(itemPrice) == -1) {
            logger.info("[${event.author.idLong}] 현재 돈(${property})보다 아이템 가격(${itemPrice})이 더 높음")
            event.channel.sendMessage("현재 돈으로 해당 아이템을 살 수가 없어요!").queue()
            return
        }

        if (UserItemDBController.addUserItem(event.author.idLong, item)) {
            UserMoneyDBController.minusMoneyUser(event.author.idLong, itemPrice)
            logger.info("[${event.author.idLong}] 지갑에서 ${item.price}원을 빼고 창고에 ${item.name}을(를) 추가함")
            event.channel.sendMessage("구매완료!").queue()
            --item
        } else
            event.channel.sendMessage("처리하는데에 오류가 발생했어요!").queue()

    }
}