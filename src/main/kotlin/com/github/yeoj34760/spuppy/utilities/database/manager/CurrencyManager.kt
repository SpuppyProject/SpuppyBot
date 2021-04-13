package com.github.yeoj34760.spuppy.utilities.database.manager

import java.math.BigDecimal

interface CurrencyManager {
    fun nowRate(currencyName: String): BigDecimal
    fun beforeRate(currencyName: String): BigDecimal
}