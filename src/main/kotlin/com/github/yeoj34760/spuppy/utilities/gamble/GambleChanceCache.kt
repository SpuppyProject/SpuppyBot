package com.github.yeoj34760.spuppy.utilities.gamble

object GambleChanceCache {
    private const val increaseValue = 2
    private const val defaultChance = 50
    private val userChanceMap: MutableMap<Long, Int> = mutableMapOf()

    fun increaseChance(id: Long) {
        if (!userChanceMap.containsKey(id)) {
            userChanceMap[id] = increaseValue + defaultChance
            return
        }

        userChanceMap[id] = userChanceMap[id]!! + increaseValue
    }

    fun currentChance(id: Long): Int {
        if (!userChanceMap.containsKey(id))
            return 50

        return userChanceMap[id]!!
    }

    fun resetChance(id: Long) = userChanceMap.remove(id)
}