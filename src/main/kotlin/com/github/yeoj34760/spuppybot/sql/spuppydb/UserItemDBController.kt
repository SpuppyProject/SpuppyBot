package com.github.yeoj34760.spuppybot.sql.spuppydb

import com.github.yeoj34760.spuppybot.item.MarketItem
import com.github.yeoj34760.spuppybot.spuppyDBConnection
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController
import java.sql.Timestamp
import java.util.*

object UserItemDBController {
    /**
     * 유저의 창고에 아이템을 추가합니다
     */
    fun addUserItem(id: Long, userItem: MarketItem) : Boolean {
        return try {
            val ps = spuppyDBConnection.prepareStatement("insert into user_item values (?,?,?) on duplicate key update count=count+1")
            ps.setString(1, userItem.name)
            ps.setInt(2, 1)
            ps.setTimestamp(3, Timestamp(Date().time))
            ps.executeQuery()
            true
        }
        catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}