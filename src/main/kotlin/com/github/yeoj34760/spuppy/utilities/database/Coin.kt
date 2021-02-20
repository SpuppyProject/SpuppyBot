package com.github.yeoj34760.spuppy.utilities.database

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object Coins : IdTable<String>("coin") {

    val name: Column<String> = text("name")
    val nameAbbreviation: Column<String> = text("name_abbreviation")
    val issuanceRate: Column<String> = text("issuance_rate")
    val issuanceCurrent: Column<String> = text("issuance_current")
    val createBy: Column<Long> = long("create_by")


    override val id: Column<EntityID<String>>
        get() = name.entityId()
}

class Coin(id: EntityID<String>) : Entity<String>(id) {
        companion object : EntityClass<String, Coin>(Coins)

    val name by Coins.name
    val nameAbbreviation by Coins.nameAbbreviation
    val issuanceRate by Coins.issuanceRate
    val issuanceCurrent by Coins.issuanceCurrent
    val createBy by Coins.createBy
}