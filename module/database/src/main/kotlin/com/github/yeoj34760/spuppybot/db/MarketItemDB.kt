package com.github.yeoj34760.spuppybot.db

import com.github.yeoj34760.spuppybot.db.`class`.MarketItem
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object MarketItemDB {
    private object Item : Table("market_item") {
        val name = text("name")
        val price = long("price")
        val count = integer("count")
        val defaultCount = integer("default_count")
        override val primaryKey = PrimaryKey(name)
    }

    fun list(): List<MarketItem> {
        val temp = mutableListOf<MarketItem>()
        transaction(DB.spuppyDB) {
            Item.selectAll().forEach {
                temp.add(MarketItem(
                        it[Item.name],
                        it[Item.price],
                        it[Item.count],
                        it[Item.defaultCount]
                ))
            }
        }
        return temp
    }

    fun minus(name: String, _count: Int = 1) = transaction(DB.spuppyDB) { Item.update({ Item.name eq name }) { with(SqlExpressionBuilder) { it[count] = count - _count } } }
    fun add(name: String, _count: Int = 1) = transaction(DB.spuppyDB) { Item.update({ Item.name eq name }) { with(SqlExpressionBuilder) { it[count] = count + _count } } }
}