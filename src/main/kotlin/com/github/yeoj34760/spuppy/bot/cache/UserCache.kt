package com.github.yeoj34760.spuppy.bot.cache

import java.math.BigInteger

interface UserCache {
    data class User(
        val id: Long,
        var money: BigInteger
    )

    fun addMoney(id: Long, sum: BigInteger): BigInteger
    fun minusMoney(id: Long, sum: BigInteger): BigInteger
    fun currentMoney(id: Long): BigInteger
}