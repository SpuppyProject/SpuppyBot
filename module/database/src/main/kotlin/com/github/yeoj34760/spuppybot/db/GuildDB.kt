package com.github.yeoj34760.spuppybot.db

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object GuildDB {
    private object Guild : Table("guild") {
        val id = long("id")
        val volume = integer("volume")

        override val primaryKey = PrimaryKey(id)
    }

    /**
     * 길드 볼륨 값을 불러옵니다. 찾을 수 없을 경우 -1로 반환합니다.
     */
    fun volume(id: Long): Int {
        transaction(DB.spuppyDB) {
            return@transaction Guild.select { Guild.id.eq(id) }.firstOrNull()?.get(Guild.volume) ?: -1
        }
        return -1
    }

    fun volume(id: Long, volume: Int) {
        transaction(DB.spuppyDB) {
            if (Guild.select { Guild.id eq id }.firstOrNull() == null)
                Guild.insert { it[Guild.id] = id; it[Guild.volume] = volume }
            else
                Guild.update({ Guild.id eq id }) { it[Guild.volume] = volume }
        }
    }

    fun remove(id: Long) {
        transaction(DB.spuppyDB) {
            if (Guild.select { Guild.id eq id }.firstOrNull() != null)
                Guild.deleteWhere { Guild.id eq id }
        }
    }
}