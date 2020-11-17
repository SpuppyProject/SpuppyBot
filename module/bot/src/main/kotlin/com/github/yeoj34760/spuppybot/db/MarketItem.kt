package com.github.yeoj34760.spuppybot.db

import com.github.yeoj34760.spuppybot.db.MarketItemDB
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
        MarketItemDB.minus(name)
        logger.info("$name count 값을 1씩 뻄")
        return MarketItem(name, price, --count_, defaultCount)
    }

    operator fun inc(): MarketItem {
        MarketItemDB.add(name)
        logger.info("$name count 값을 1씩 추가")
        return MarketItem(name, price, ++count_, defaultCount)
    }

    fun add(count: Int) {
        MarketItemDB.add(name, count)
        logger.info("$name count 값을 ${count}씩 추가")
    }

    fun minus(count: Int) {
        if (this.count <= count) {
            logger.info("$name count 값을 ${this.count}씩 뺌")
            MarketItemDB.minus(name, this.count)
        } else {
            logger.info("$name count 값을 ${count}씩 뺌")
            MarketItemDB.minus(name, count)
        }
    }
}