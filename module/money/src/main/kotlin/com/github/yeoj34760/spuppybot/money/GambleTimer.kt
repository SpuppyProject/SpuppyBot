package com.github.yeoj34760.spuppybot.money

import java.util.*
import kotlin.concurrent.timer
import kotlin.random.Random

object GambleTimer {
    var nowGamblingProbability: Int = 0
    var updateGamblingProbability: Date = Date()
    const val TIMER = 5

    fun start() {
        timer(period = 60 * TIMER.toLong() * 1000) {
            nowGamblingProbability = Random.nextInt(40, 65)
            val cal = Calendar.getInstance()
            cal.add(Calendar.MINUTE, TIMER)
            updateGamblingProbability = cal.time
        }
    }
}