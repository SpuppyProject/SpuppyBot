package com.github.yeoj34760.spuppybot.market

import com.github.yeoj34760.spuppybot.db.`class`.MarketItem
import com.github.yeoj34760.spuppybot.db.MarketItemDBController

object MarketItemList {
    private var marketItemListCache: List<MarketItem> = MarketItemDBController.marketItemList()

    operator fun get(name: String): MarketItem? {
        for (item in marketItemListCache)
            if (item.name == name)
                return item
        return null
    }

    fun reload() {
        marketItemListCache = MarketItemDBController.marketItemList()
    }
}