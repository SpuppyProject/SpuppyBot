package com.github.yeoj34760.spuppy.bot.cache

import com.github.yeoj34760.spuppy.bot.Bot
import com.github.yeoj34760.spuppy.bot.database.DatabaseUser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigInteger

object MariaUserCache : UserCache {

    private val userList: MutableList<UserCache.User> = mutableListOf()
    private val updateUserList: MutableList<UserCache.User> = mutableListOf()

    init {
        transaction(Bot.mainDB) {
            DatabaseUser.all().forEach {
                val tempUser = UserCache.User(it.id.value, BigInteger(it.money))
                userList.add(tempUser)
            }
        }

        GlobalScope.launch {
            while (true) {
                delay(1000 * 10)
                if (updateUserList.isEmpty())
                    continue

                transaction(Bot.mainDB) {
                    updateUserList.forEach {
                        DatabaseUser[it.id].money = it.money.toString()
                    }
                }

                updateUserList.clear()
            }
        }
    }

    override fun addMoney(id: Long, sum: BigInteger): BigInteger {
        val user = userList.first { it.id == id }
        val updateUser = updateUserList.firstOrNull { it.id == id }
        val result = user.money.add(sum)
        user.money = result

        when (updateUser) {
            null -> updateUserList.add(user.copy())
            else -> updateUser.money = result
        }

        return result
    }

    override fun minusMoney(id: Long, sum: BigInteger): BigInteger {
        val user = userList.first { it.id == id }
        val updateUser = updateUserList.firstOrNull { it.id == id }
        val result = user.money.minus(sum)
        user.money = result

        when (updateUser) {
            null -> updateUserList.add(user.copy())
            else -> updateUser.money = result
        }

        return result
    }

    override fun currentMoney(id: Long): BigInteger = userList.first { it.id == id }.money
}