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

    fun add(count: Int) {
        MarketItemDBController.addMarketItem(name, count)
    }

    fun minus(count: Int) {
        if (this.count <= count)
            MarketItemDBController.minusMarketItem(name, this.count)
        else
            MarketItemDBController.minusMarketItem(name, count)
    }
}