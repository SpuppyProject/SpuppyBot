package com.github.yeoj34760.spuppybot.sql.spuppydb

import com.github.yeoj34760.spuppybot.item.MarketItem
import com.github.yeoj34760.spuppybot.market.MarketItemList
import com.github.yeoj34760.spuppybot.spuppyDBConnection
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController

object MarketItemDBController {
    /**
     * 마트에 있는 아이템들을 반환합니다.
     */
    fun marketItemList() : List<MarketItem> {
        val t=   spuppyDBConnection.createStatement().executeQuery("select * from market_item")
        val temp = mutableListOf<MarketItem>()
        while (t.next()) {
            val tempItem = MarketItem(
                    t.getString(1),
                    t.getLong(2),
                    t.getInt(3),
                    t.getInt(4)
            )
            temp.add(tempItem)
        }

        return temp
    }

    /**
     * 마켓에 있는 아이템 갯수를 하나뺍니다.
     */
    fun minusMarketItem(name: String, count: Int = 1) {
        val ps = spuppyDBConnection.prepareStatement("update market_item set count=count-? where name=?")
        ps.setInt(1, count)
        ps.setString(2, name)

        ps.execute()
    }

    /**
     * 마켓에 있는 아이템 갯수를 하나 추가합니다.
     */
    fun addMarketItem(name: String, count: Int = 1) {
        val ps = spuppyDBConnection.prepareStatement("update market_item set count=count+? where name=?")
        ps.setInt(1, count)
        ps.setString(2, name)

        ps.execute()
    }
}