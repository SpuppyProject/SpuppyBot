package com.github.yeoj34760.spuppy.fixer

import com.github.yeoj34760.spuppy.bot.Bot
import org.junit.Test


class FixerTest {
    @Test
    fun allTest() {
        val fixer = Fixer.create(Bot.info.fixerKey) ?: error("fixer is null")
        println(fixer)
        println(fixer.changeBase("USD"))
        println(fixer.changeBase("KRW"))
    }
}