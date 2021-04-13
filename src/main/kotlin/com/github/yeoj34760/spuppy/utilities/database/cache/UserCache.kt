package com.github.yeoj34760.spuppy.utilities.database.cache

import com.github.yeoj34760.spuppy.utilities.database.manager.MoneyManager
import java.math.BigDecimal

interface UserCache {
    data class User(
        val id: Long,
        var money: BigDecimal
    )

    fun money(id: Long): MoneyManager
}