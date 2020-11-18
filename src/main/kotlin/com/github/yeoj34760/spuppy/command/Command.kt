package com.github.yeoj34760.spuppy.command

abstract class Command(val name: String, val alias: List<String>, var isAllow: Boolean = true) {
    abstract fun execute(event: CommandEvent)
}