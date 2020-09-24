package com.github.yeoj34760.spuppybot.sql.spuppydb

import com.github.yeoj34760.spuppybot.item.MarketItem
import com.github.yeoj34760.spuppybot.item.UserItem
import com.github.yeoj34760.spuppybot.spuppyDBConnection
import java.sql.PreparedStatement
import java.sql.Timestamp
import java.util.*

object UserItemDBController {
    /**
     * 유저의 창고에 아이템을 추가합니다
     */
    fun addUserItem(id: Long, marketItem: MarketItem): Boolean {
        return try {
            if (checkUserItem(id, marketItem.name))
                addUserItemUpdate(id, marketItem)
            else
                addUserItemInsert(id, marketItem)

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun addUserItemInsert(id: Long, marketItem: MarketItem) {
        val ps: PreparedStatement = spuppyDBConnection.prepareStatement("insert into user_item values (?,?,?,?)")
        ps.setLong(1, id)
        ps.setString(2, marketItem.name)
        ps.setInt(3, 1)
        ps.setTimestamp(4, Timestamp(Date().time))
        ps.execute()
    }

    private fun addUserItemUpdate(id: Long, marketItem: MarketItem) {
        val ps: PreparedStatement = spuppyDBConnection.prepareStatement("update user_item set count=count+1, timestamp=? where id=? and name=?")
        ps.setTimestamp(1, Timestamp(Date().time))
        ps.setLong(2, id)
        ps.setString(3, marketItem.name)
        ps.execute()
    }


    fun minusUserItem(id: Long, marketItem: MarketItem): Boolean {
        return try {
            if (!checkUserItem(id, marketItem.name))
                return false

            if (get(id, marketItem.name)!!.count == 0)
                minusUserItemDelete(id, marketItem)
            else
                minusUserItemUpdate(id, marketItem)

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun minusUserItemDelete(id: Long, marketItem: MarketItem) {
        val ps: PreparedStatement = spuppyDBConnection.prepareStatement("delete from user_item where id=? and name=?")
        ps.setLong(1, id)
        ps.setString(2, marketItem.name)
        ps.execute()
    }

    private fun minusUserItemUpdate(id: Long, marketItem: MarketItem) {
        val ps: PreparedStatement = spuppyDBConnection.prepareStatement("update user_item set count=count-1, timestamp=? where id=? and name=?")
        ps.setTimestamp(1, Timestamp(Date().time))
        ps.setLong(2, id)
        ps.setString(3, marketItem.name)
        ps.execute()
    }


    fun fromUserItemList(id: Long): List<UserItem> {
        val ps = spuppyDBConnection.prepareStatement("select name,count,timestamp from user_item where id=?")
        ps.setLong(1, id)
        val t = ps.executeQuery()
        val tempItemList = mutableListOf<UserItem>()
        while (t.next())
            tempItemList.add(UserItem(t.getString(1), t.getInt(2), t.getTimestamp(3)))

        return tempItemList
    }

    fun checkUserItem(id: Long, itemName: String): Boolean {
        val ps = spuppyDBConnection.prepareStatement("select exists(select * from user_item where id=? and name=?)")
        ps.setLong(1, id)
        ps.setString(2, itemName)
        val result = ps.executeQuery()
        if (result.next())
            return result.getInt(1) == 1

        return false
    }

    operator fun get(id: Long, name: String) : UserItem? {
     val ps = spuppyDBConnection.prepareStatement("select name, count, timestamp from user_item where id=? and name=?")
        ps.setLong(1, id)
        ps.setString(2, name)
        val result = ps.executeQuery()
        if (result.next())
            return UserItem(result.getString(1),
            result.getInt(2),
            result.getTimestamp(3))

        return null
    }
}