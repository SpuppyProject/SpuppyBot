package com.github.yeoj34760.spuppy.fixer

import com.github.yeoj34760.spuppy.bot.Bot
import org.junit.Test


class FixerTest {
    @Test
    fun changeTest() {
        val fixer = Fixer.create(Bot.info.fixerKey)
        println(fixer!!.changeBase("KRW"))
        println(fixer.changeBase("USD"))
    }
}