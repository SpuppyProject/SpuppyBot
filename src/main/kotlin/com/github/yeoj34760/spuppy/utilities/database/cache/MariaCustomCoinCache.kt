package com.github.yeoj34760.spuppy.utilities.database.cache

import com.github.yeoj34760.spuppy.bot.Bot
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object MariaCustomCoinCache : CustomCoinCache {
    private object AsicModelTable: Table("asic_model") {
        val asicId: Column<String> = text("asic_id")
        val name: Column<String> = text("name")
        val company: Column<String> = text("company")
        val performance: Column<Int> = integer("performance")
        val price: Column<Int> = integer("price")
    }

    private val asicModelList: MutableList<AsicModel>
    init {
        val tempList: MutableList<AsicModel> = mutableListOf()
//        transaction(Bot.coinDB) {
//            AsicModelTable.selectAll().forEach {
//                tempList.add(
//                    AsicModel(
//                        it[AsicModelTable.asicId],
//                        it[AsicModelTable.name],
//                        it[AsicModelTable.company],
//                        it[AsicModelTable.performance],
//                        it[AsicModelTable.price]
//                    )
//                )
//            }
//        }

        asicModelList = tempList
    }
    override fun asicModels(): List<AsicModel> = asicModelList
}