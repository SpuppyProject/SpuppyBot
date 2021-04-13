package com.github.yeoj34760.spuppy.utilities.database.cache

import com.github.yeoj34760.spuppy.bot.Bot
import com.github.yeoj34760.spuppy.utilities.database.DatabaseUser
import com.github.yeoj34760.spuppy.utilities.database.manager.MoneyManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

object MariaUserCache : UserCache {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
    private val userList: MutableList<UserCache.User> = mutableListOf()

    /**
     * 데이터베이스에 업데이트하기 전 유저정보들입니다. 데이터베이스에 저장하게 되면 자동으로 clean합니다.
     */
    private val updateUserList: MutableList<UserCache.User> = mutableListOf()
    private var autoDatabaseUpdateJob: Job? = null

    //데이터베이스에 있는 유저 정보를 userList에 저장합니다.
    init {
        transaction(Bot.mainDB) {
            DatabaseUser.all().forEach {
                val tempUser = UserCache.User(it.id.value, BigDecimal(it.money))
                userList.add(tempUser)
            }
        }

        startAutoDatabaseUpdate()
    }

    //주기적으로 updateUserList에 저장되어 있는 정보들을 데이터베이스에 저장합니다.
    private fun startAutoDatabaseUpdate() {
        autoDatabaseUpdateJob = GlobalScope.launch {
            while (true) {
                delay(1000 * 10)
                if (updateUserList.isEmpty())
                    continue

                log.info("유저 정보를 데이터베이스에 업데이트합니다.")
                transaction(Bot.mainDB) {
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

    /**
     * 자동으로 데이터베이스에 저장하는 걸 멈춥니다.
     */
    private fun stopAutoDatabaseUpdate() {
        autoDatabaseUpdateJob?.cancel()
    }

    override fun money(id: Long): MoneyManager = MariaMoneyManager(id)

    class MariaMoneyManager(private val id: Long) : MoneyManager {
        override fun plusAssign(value: BigDecimal) {
            val user = userList.first {it.id == id}
            update(user, user.money.add(value.round(MathContext(4))), value)
        }

        override fun minusAssign(value: BigDecimal) {
            val user = userList.first {it.id == id}
            update(user, user.money.minus(value.round(MathContext(4))), value)
        }

        override fun current(): BigDecimal = userList.first { it.id == id }.money
        private fun update(user: UserCache.User, result: BigDecimal, value: BigDecimal) {
            val updateUser = updateUserList.firstOrNull { it.id == id }
            log.info("<{}> update money: {}, {} to {}", id, value, user.money, result)
            user.money = result

            when (updateUser) {
                null -> updateUserList.add(user.copy())
                else -> updateUser.money = result
            }
        }
    }
}