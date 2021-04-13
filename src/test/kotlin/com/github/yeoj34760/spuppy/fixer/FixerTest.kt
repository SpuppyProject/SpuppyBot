package com.github.yeoj34760.spuppy.fixer

import org.junit.Test


class FixerTest {
    @Test
    fun createTest() {
        val fixer = Fixer.create("API_KEY")
        println(fixer)
    }

    @Test
    fun changeTest() {
        val fixer = Fixer.create("API_KEY")
        println(fixer!!.changeBase("KRW"))
        println(fixer!!.changeBase("USD"))

    }
}