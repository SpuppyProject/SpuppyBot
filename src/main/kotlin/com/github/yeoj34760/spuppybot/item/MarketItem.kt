package com.github.yeoj34760.spuppybot.item

import com.github.yeoj34760.spuppybot.sql.spuppydb.MarketItemDBController

data class MarketItem(
        val name: String,
        val price: Long,
        private var count_ : Int,
        val defaultCount: Int
) {
    val count: Int get() = count_
    operator fun dec(): MarketItem {
        MarketItemDBController.minusMarketItem(name)
        return MarketItem(name, price, --count_, defaultCount)
    }

    operator fun inc(): MarketItem {
     MarketItemDBController.addMarketItem(name)
        return MarketItem(name, price, ++count_, defaultCount)
    }
}