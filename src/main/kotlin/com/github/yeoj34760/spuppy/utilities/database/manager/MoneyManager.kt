package com.github.yeoj34760.spuppy.utilities.database.manager

import java.math.BigDecimal

interface MoneyManager {
    operator fun plusAssign(value: BigDecimal)
    operator fun minusAssign(value: BigDecimal)
    fun set(value: BigDecimal)
    fun current(): BigDecimal
}