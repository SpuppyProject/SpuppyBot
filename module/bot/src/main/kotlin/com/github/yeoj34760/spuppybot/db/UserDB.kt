package com.github.yeoj34760.spuppybot.db


import com.github.yeoj34760.spuppybot.TrackManager
import com.github.yeoj34760.spuppybot.db.user.UserInfo
import com.github.yeoj34760.spuppybot.db.user.UserItem
import com.github.yeoj34760.spuppybot.db.user.UserPlaylist
import com.github.yeoj34760.spuppybot.db.user.UserPlaylistManager
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.jodatime.datetime
import org.jetbrains.exposed.sql.statements.UpdateStatement
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.math.BigInteger
import java.util.*
import kotlin.collections.HashMap

class UserDB(private val userId: Long) {

    private object UserTable : Table("user") {
        val id = long("id")
        val money = text("money")
        val receiveMoney = datetime("receive_money")

        //        val box = text("box")
        val playlist = text("playlist")
        val items = text("items")
    }

    companion object {
        val idListCache = idList()
        private fun idList(): MutableList<Long> {
            val temp = mutableListOf<Long>()
            transaction(DB.spuppyDB) { UserTable.selectAll().forEach { temp.add(it[UserTable.id]) } }
            return temp
        }

        fun idCheck(id: Long): Boolean = idList().find { it == id } != null
    }

    fun create() {
        transaction(DB.spuppyDB) {
            if (UserTable.select { UserTable.id eq userId }.firstOrNull() == null) {
                UserTable.insert {
                    it[id] = userId
                    it[receiveMoney] = DateTime.now()
                }
                idListCache.add(this@UserDB.userId)
            }

        }
    }

    fun remove() {
        transaction(DB.spuppyDB) {
            if (UserTable.select { UserTable.id eq userId }.firstOrNull() != null) {
                UserTable.deleteWhere { UserTable.id eq userId }
                idListCache.remove(this@UserDB.userId)
            }
        }
    }

    //    fun boxAdd(track: AudioTrack) {
    fun playlistAdd(name: String, track: AudioTrack) {
        transaction(DB.spuppyDB) {
            val user = user() ?: throw Exception("유저 등록안되어 있음")
            val temp = if (user[UserTable.playlist].isEmpty()) UserPlaylist() else fromPlaylist()
            if (!temp.containsKey(name))
                return@transaction


            UserPlaylistManager.addTrack(temp, name, track)
            playlistUpdate(Base.encode(Json.encodeToString(temp)))
        }
    }

    fun createPlaylist(name: String) {
        transaction(DB.spuppyDB) {
            val user = user() ?: throw Exception("유저 등록안되어 있음")

//            if (user[UserTable.playlist].isEmpty()) {
//                playlistUpdate(Base.encode(Json.encodeToString(UserPlaylist())))
//            }

            val temp = if (user[UserTable.playlist].isEmpty()) UserPlaylist() else fromPlaylist()
            if (temp.containsKey(name))
                return@transaction
            temp[name] = mutableListOf()
            playlistUpdate(Base.encode(Json.encodeToString(temp as HashMap<String, MutableList<String>>)))
        }


    }

    fun boxRemove(name: String, num: Int) {
        transaction(DB.spuppyDB) {
            if (UserTable.select { UserTable.id eq userId }.firstOrNull() == null) {
                throw Exception("유저 아이디 등록되어 있지 않음")
            }
            val temp = fromPlaylist()
            if (!temp.containsKey(name))
                return@transaction

            temp[name]?.removeAt(num)
            playlistUpdate(Gson().toJson(temp))
        }
    }

    fun boxMove(name: String, num1: Int, num2: Int) {
        transaction(DB.spuppyDB) {
            if (UserTable.select { UserTable.id eq userId }.firstOrNull() == null) {
                throw Exception("유저 아이디 등록되어 있지 않음")
            }
            val temp = fromPlaylist()
            val tracks = temp[name] ?: throw Exception("해당 이름을 가진 플레이리스트를 찾을 수 없음!\nuserId: ${userId}\nplaylist name: ${name}")
            val tempTrack = tracks[num1]
            tracks.remove(tempTrack)
            tracks.add(num2, tempTrack)
            playlistUpdate(Gson().toJson(temp))
        }
    }

    fun boxRemoveAll() {
        transaction(DB.spuppyDB) {
            if (UserTable.select { UserTable.id eq userId }.firstOrNull() == null) {
                throw Exception("유저 아이디 등록되어 있지 않음")
            }
            val temp = fromPlaylist()
            temp.clear()
            playlistUpdate(Gson().toJson(temp))
        }
    }

   private fun fromPlaylist(): UserPlaylist {
        return transaction(DB.spuppyDB) {
            val str = UserTable.select { UserTable.id eq userId }.first()[UserTable.playlist]
            val t = Json {ignoreUnknownKeys = true}.decodeFromString<HashMap<String, List<String>>>(Base.decode(str))
            println(t)

            return@transaction Json {ignoreUnknownKeys = true}.decodeFromString(Base.decode(str))
        }
    }

    //    fun boxUpdate(box: String) = update { it[UserTable.box] = box }
   private fun playlistUpdate(playlist: String) = update { it[UserTable.playlist] = playlist }

    @ExperimentalSerializationApi
    fun itemAdd(name: String) {
        transaction(DB.spuppyDB) {
            val user = user() ?: throw Exception("유저 등록안되어 있음")
            if (user[UserTable.items].isEmpty()) {
                val userItemList = mutableListOf<UserItem>()
                userItemList.add(UserItem(name, 1, DateTime.now()))
                itemsUpdate(String(Base64.getEncoder().encode(Json.encodeToString(userItemList).toByteArray())))
                return@transaction
            }

            val itemList = Json.decodeFromString<List<UserItem>>(String(Base64.getDecoder().decode(user[UserTable.items]))).toMutableList()
            val userItem = item(name, itemList)
            if (userItem != null)
                userItem.count++
            else
                itemList.add(UserItem(name, 1, DateTime.now()))

            itemsUpdate(String(Base64.getEncoder().encode(Json.encodeToString(itemList).toByteArray())))
        }
    }

    fun itemMinus(name: String, count: Int = 1) {
        transaction(DB.spuppyDB) {
            val user = user() ?: throw Exception("유저 등록안되어 있음")
            val itemList = Json.decodeFromString<List<UserItem>>(String(Base64.getDecoder().decode(user[UserTable.items]))).toMutableList()
            val userItem = item(name, itemList)
            if (userItem != null) {
                userItem.count -= count
                if (userItem.count <= 0)
                    itemList.remove(userItem)
            } else
                return@transaction

            itemsUpdate(String(Base64.getEncoder().encode(Json.encodeToString(itemList).toByteArray())))
        }
    }

    private fun item(name: String, itemList: List<UserItem>): UserItem? {
        val filterList = itemList.filter { it.name == name }
        return if (filterList.isEmpty()) null else filterList[0]
    }

    fun itemsUpdate(items: String) = update { it[UserTable.items] = items }
    fun moneyUpdate(money: BigInteger) = update { it[UserTable.money] = money.toString() }
    fun receiveMoneyUpdate() = update { it[receiveMoney] = DateTime.now().plusMinutes(1) }


    private fun update(body: UserTable.(UpdateStatement) -> Unit) {
        transaction(DB.spuppyDB) {
            if (UserTable.select { UserTable.id eq userId }.firstOrNull() != null)
                UserTable.update({ UserTable.id eq userId }, body = body)
        }
    }

    operator fun invoke(): UserInfo {
        return transaction(DB.spuppyDB) {
            if (UserTable.select { UserTable.id eq userId }.firstOrNull() == null)
                throw Exception("유저 아이디 등록이 안되었음")

            val temp: UserInfo
            val sel = UserTable.select { UserTable.id eq userId }.first()
            val playlist = if (sel[UserTable.playlist].isEmpty()) UserPlaylist() else fromPlaylist()
            val itemList = if (sel[UserTable.items].isEmpty()) mutableListOf() else Json.decodeFromString<List<UserItem>>(String(Base64.getDecoder().decode(sel[UserTable.items])))
            temp = UserInfo(userId, BigInteger(sel[UserTable.money]), sel[UserTable.receiveMoney], playlist, itemList)
            return@transaction temp
        }
    }

    private fun user(): ResultRow? {
        return transaction(DB.spuppyDB) {
            return@transaction UserTable.select { UserTable.id eq userId }.firstOrNull()
        }
    }

    fun check(): Boolean {
        return transaction(DB.spuppyDB) {
            println(UserTable.select { UserTable.id eq userId }.firstOrNull())
            return@transaction UserTable.select { UserTable.id eq userId }.firstOrNull() != null
        }
    }
}