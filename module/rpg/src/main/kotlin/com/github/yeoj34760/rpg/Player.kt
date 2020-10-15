package com.github.yeoj34760.rpg

import com.github.yeoj34760.rpg.weapon.Weapon
import com.github.yeoj34760.spuppybot.db.DB
import com.github.yeoj34760.spuppybot.db.rpg.PlayerTable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.dv8tion.jda.api.entities.User
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime
import java.util.*


fun User.player(): Player = Player.create(this.idLong)

data class Player(val id: Long,
                  var weapon: Weapon,
                  var level: Int,
                  var monsterKill: Int,
                  val weaponList: MutableList<PlayerWeapon>) {

    fun addWeapon(weapon: Weapon) {
        transaction(DB.rpgDB) {
            val temp = weaponList.firstOrNull { it.name == weapon.name }
            when (temp) {
                null -> weaponList.add(PlayerWeapon(weapon.name, DateTime.now(), 1))
                else -> weaponList.first { it.name == weapon.name }.count++
            }

            val encode = Base64.getEncoder().encodeToString(Json.encodeToString(weaponList).toByteArray())
            PlayerTable.update({ PlayerTable.id eq this@Player.id }) { it[weaponList] = encode }
        }
    }

    fun replaceWeapon(playerWeapon: PlayerWeapon) {
        transaction(DB.rpgDB) {
            when (--playerWeapon.count) {
                0 -> weaponList.remove(playerWeapon)
            }

            when (val oldWeapon = weaponList.firstOrNull { it.name == this@Player.weapon.name }) {
                null -> weaponList.add(PlayerWeapon(weapon.name, DateTime.now(), 1))
                else -> oldWeapon.count++
            }


            this@Player.weapon = rpg.weaponList.first {it.name == playerWeapon.name}


            val encode = Base64.getEncoder().encodeToString(Json.encodeToString(weaponList).toByteArray())
            PlayerTable.update({ PlayerTable.id eq this@Player.id }) {
                it[weaponList] = encode
                it[weapon] = playerWeapon.name
            }
        }
    }

    fun addMonsterKill() {
        transaction(DB.rpgDB) { PlayerTable.update({ PlayerTable.id eq this@Player.id }) { with(SqlExpressionBuilder) { it[monsterKill] = monsterKill + 1 } } }
    }


    companion object {
        fun create(id: Long): Player = transaction(DB.rpgDB) {
            val playerTable = PlayerTable.select { PlayerTable.id eq id }.first()
            val weapon = rpg.weaponList.first { it.name == playerTable[PlayerTable.weapon] }
            val level = playerTable[PlayerTable.level]
            val monsterKill = playerTable[PlayerTable.monsterKill]
            val weaponList = mutableListOf<PlayerWeapon>()
            if (playerTable[PlayerTable.weaponList].isNotEmpty()) {
                val decode = String(Base64.getDecoder().decode(playerTable[PlayerTable.weaponList]))
                Json.decodeFromString<List<PlayerWeapon>>(decode).forEach { weaponList.add(it) }
            }
            Player(id, weapon, level, monsterKill, weaponList)
        }
    }
}