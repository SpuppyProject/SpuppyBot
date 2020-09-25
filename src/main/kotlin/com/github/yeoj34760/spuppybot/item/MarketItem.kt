package com.github.yeoj34760.spuppybot.item

import com.github.yeoj34760.spuppybot.sql.spuppydb.MarketItemDBController
import org.slf4j.Logger
import org.slf4j.LoggerFactory

data class MarketItem(
        val name: String,
        val price: Long,
        private var count_: Int,
        val defaultCount: Int
) {
    private val logger: Logger = LoggerFactory.getLogger("상점 아이템")
    val count: Int get() = count_
    operator fun dec(): MarketItem {
        MarketItemDBController.minusMarketItem(name)
        logger.info("$name count 값을 1씩 뻄")
        return MarketItem(name, price, --count_, defaultCount)
    }

    operator fun inc(): MarketItem {
        MarketItemDBController.addMarketItem(name)
        logger.info("$name count 값을 1씩 추가")
        return MarketItem(name, price, ++count_, defaultCount)
    }

    fun add(count: Int) {
        logger.info("$name count 값을 ${count}씩 추가")
        MarketItemDBController.addMarketItem(name, count)
    }

    fun minus(count: Int) {
        if (this.count <= count) {
            logger.info("$name count 값을 ${this.count}씩 뺌")
            MarketItemDBController.minusMarketItem(name, this.count)
        } else {
            logger.info("$name count 값을 ${count}씩 뺌")
            MarketItemDBController.minusMarketItem(name, count)
        }
    }
}