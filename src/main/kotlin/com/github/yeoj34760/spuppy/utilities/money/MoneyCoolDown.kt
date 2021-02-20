package com.github.yeoj34760.spuppy.utilities.money

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object MoneyCoolDown {
    private const val CoolDownTime: Long = 60000
    private val userCoolDownMap: MutableMap<Long, Long> = mutableMapOf()
    fun start(id: Long) {
        if (check(id))
            return

        userCoolDownMap[id] = System.currentTimeMillis() + CoolDownTime
        GlobalScope.launch {
            delay(CoolDownTime)
            userCoolDownMap.remove(id)
        }
    }

    fun check(id: Long): Boolean = userCoolDownMap.containsKey(id)

    fun remainingTime(id: Long): Long = userCoolDownMap[id]!! - System.currentTimeMillis()
}