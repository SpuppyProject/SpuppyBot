package com.github.yeoj34760.spuppybot.money.command.market

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.db.MarketItemDB
import com.github.yeoj34760.spuppybot.db.UserDB
import com.github.yeoj34760.spuppybot.db.`class`.MarketItem
import com.github.yeoj34760.spuppybot.db.user.info
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigInteger


@CommandSettings(name = "buymarket")
object BuyMarket : Command() {
    private val logger: Logger = LoggerFactory.getLogger("마켓 구매")
    override fun execute(event: CommandEvent) {
        var item: MarketItem? = MarketItemDB.list().filter { it.name == event.content }[0]

        if (item == null) {
            event.channel.sendMessage("해당 아이템이 상점에 없습니다.").queue()
            return
        }

        if (item.count <= 0) {
            event.channel.sendMessage("해당 아이템은 재고가 없네요.").queue()
            return
        }

        val property = event.author.info.money
        val itemPrice = BigInteger(item.price.toString())
        if (property.compareTo(itemPrice) == -1) {
            logger.info("[${event.author.idLong}] 현재 돈(${property})보다 아이템 가격(${itemPrice})이 더 높음")
            event.channel.sendMessage("현재 돈으로 해당 아이템을 살 수가 없어요!").queue()
            return
        }

            val userDB = UserDB(event.author.idLong)
            userDB.itemAdd(item.name)
            userDB.moneyUpdate(property.minus(itemPrice))
            logger.info("[${event.author.idLong}] 지갑에서 ${item.price}원을 빼고 창고에 ${item.name}을(를) 추가함")
            event.channel.sendMessage("구매완료!").queue()
            --item
    }
}