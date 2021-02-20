package com.github.yeoj34760.spuppy.utilities.database.cache

import com.github.yeoj34760.spuppy.bot.Bot
import com.github.yeoj34760.spuppy.utilities.database.DatabaseUser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigInteger

object MariaUserCache : UserCache {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
    private val userList: MutableList<UserCache.User> = mutableListOf()
    private val updateUserList: MutableList<UserCache.User> = mutableListOf()
    private var autoDatabaseUpdateJob: Job? = null

    init {
        transaction(Bot.mainDB) {
            DatabaseUser.all().forEach {
                val tempUser = UserCache.User(it.id.value, BigInteger(it.money))
                userList.add(tempUser)
            }
        }

        startAutoDatabaseUpdate()
    }

    private fun startAutoDatabaseUpdate() {
        autoDatabaseUpdateJob = GlobalScope.launch {
            while (true) {
                delay(1000 * 10)
                if (updateUserList.isEmpty())
                    continue

                log.info("유저 정보를 데이터베이스에 업데이트합니다.")
                transaction(Bot.mainDB) {
//                    updateUserList.forEach {
//                        log.info("update database: {} to {}", DatabaseUser[it.id].money, it.money)
//                        DatabaseUser[it.id].money = it.money.toString()
//                    }

                    DatabaseUser.forIds(updateUserList.map { it.id }).forEach { user ->
                        val updateUserMoney = updateUserList.first { it.id == user.id.value }.money.toString()
                        log.info("update database: {} to {}", user.money, updateUserMoney)
                        user.money = updateUserMoney
                    }
                }

                updateUserList.clear()
            }
        }
    }

    private fun stopAutoDatabaseUpdate() {
        autoDatabaseUpdateJob?.cancel()
    }

    override fun addMoney(id: Long, sum: BigInteger): BigInteger {

        val user = userList.first { it.id == id }
        val updateUser = updateUserList.firstOrNull { it.id == id }
        val result = user.money.add(sum)
        log.info("<{}> add money: {}, {} to {}", id, sum, user.money, result)
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
        log.info("<{}> minus money: {}, {} to {}", id, sum, user.money, result)
        user.money = result

        when (updateUser) {
            null -> updateUserList.add(user.copy())
            else -> updateUser.money = result
        }

        return result
    }

    override fun currentMoney(id: Long): BigInteger = userList.first { it.id == id }.money
}