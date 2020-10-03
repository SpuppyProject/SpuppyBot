package com.github.yeoj34760.spuppybot.db


import com.github.yeoj34760.spuppybot.TrackManager
import com.github.yeoj34760.spuppybot.db.user.UserInfo
import com.github.yeoj34760.spuppybot.db.user.UserItem
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.jodatime.datetime
import org.jetbrains.exposed.sql.statements.UpdateStatement
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.math.BigInteger
import java.util.*

class UserDB(private val userId: Long) {

    private object UserTable : Table("user") {
        val id = long("id")
        val money = text("money")
        val receiveMoney = datetime("receive_money")
        val box = text("box")
        val items = text("items")
    }

    companion object {
        val userIdList = IdList()
        private fun IdList(): MutableList<Long> {
            val temp = mutableListOf<Long>()
            transaction(DB.spuppyDB) { UserTable.selectAll().forEach { temp.add(it[UserTable.id]) } }
            return temp
        }

        fun idCheck(id: Long) : Boolean = userIdList.find { it == id } != null
    }

    fun create() {
        transaction(DB.spuppyDB) {
            if (UserTable.select { UserTable.id eq userId }.firstOrNull() == null) {
                UserTable.insert {
                    it[UserTable.id] = userId
                    it[receiveMoney] = DateTime.now()
                }
                userIdList.add(userId)
            }

        }
    }

    fun remove() {
        transaction(DB.spuppyDB) {
            if (UserTable.select { UserTable.id eq userId }.firstOrNull() != null) {
                UserTable.deleteWhere { UserTable.id eq userId }
                userIdList.remove(userId)
            }
        }
    }

    fun boxAdd(track: AudioTrack) {
        transaction(DB.spuppyDB) {
            val user = user() ?: throw Exception("유저 등록안되어 있음")
            if (user[UserTable.box].isEmpty()) {
                val tempJsonObject = JsonObject()
                val tempJsonArray = JsonArray()
                tempJsonArray.add(TrackManager.trackToBase64(track))
                tempJsonObject.add("tracks", tempJsonArray)
                boxUpdate(Base64.getEncoder().encodeToString(Gson().toJson(tempJsonObject).toByteArray()))
                return@transaction
            }

            val temp = baseToTrackList(UserTable.select { UserTable.id eq userId }.first()[UserTable.box]).toMutableList()
            temp.add(track)
            boxUpdate(trackListToBase64(temp))
        }
    }

    fun boxRemove(num: Int) {
        transaction(DB.spuppyDB) {
            if (UserTable.select { UserTable.id eq userId }.firstOrNull() == null) {
                throw Exception("유저 아이디 등록되어 있지 않음")
            }
            val temp = baseToTrackList(UserTable.select { UserTable.id eq userId }.first()[UserTable.box]).toMutableList()
            temp.removeAt(num)
            boxUpdate(trackListToBase64(temp))
        }
    }

    fun boxMove(num1: Int, num2: Int) {
        transaction(DB.spuppyDB) {
            if (UserTable.select { UserTable.id eq userId }.firstOrNull() == null) {
                throw Exception("유저 아이디 등록되어 있지 않음")
            }
            val temp = baseToTrackList(UserTable.select { UserTable.id eq userId }.first()[UserTable.box]).toMutableList()
            val tempTrack = temp[num1]
            temp.remove(tempTrack)
            temp.add(num2, tempTrack)
            boxUpdate(trackListToBase64(temp))
        }
    }

    fun boxRemoveAll() {
        transaction(DB.spuppyDB) {
            if (UserTable.select { UserTable.id eq userId }.firstOrNull() == null) {
                throw Exception("유저 아이디 등록되어 있지 않음")
            }
            val temp = baseToTrackList(UserTable.select { UserTable.id eq userId }.first()[UserTable.box]).toMutableList()
            temp.removeAll { true }
            boxUpdate(trackListToBase64(temp))
        }
    }

    fun boxUpdate(box: String) = update { it[UserTable.box] = box }

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
            val boxList = if (sel[UserTable.box].isEmpty()) mutableListOf() else baseToTrackList(sel[UserTable.box])
            val itemList = if (sel[UserTable.items].isEmpty()) mutableListOf() else Json.decodeFromString<List<UserItem>>(String(Base64.getDecoder().decode(sel[UserTable.items])))
            temp = UserInfo(userId, BigInteger(sel[UserTable.money]), sel[UserTable.receiveMoney], boxList, itemList)
            return@transaction temp
        }
    }

    private fun baseToTrackList(base64: String): List<AudioTrack> {
        val tempList = mutableListOf<AudioTrack>()
        val tempJsonObject = Gson().fromJson(String(Base64.getDecoder().decode(base64)), JsonObject::class.java)
        tempJsonObject["tracks"].asJsonArray.forEach { tempList.add(TrackManager.base64ToTrack(it.asString)) }
        return tempList
    }

    private fun trackListToBase64(tracks: List<AudioTrack>): String {
        val tempJsonObject = JsonObject()
        val tempJsonArray = JsonArray()
        tracks.forEach { tempJsonArray.add(TrackManager.trackToBase64(it)) }
        tempJsonObject.add("tracks", tempJsonArray)

        return Base64.getEncoder().encodeToString(Gson().toJson(tempJsonObject).toByteArray())
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