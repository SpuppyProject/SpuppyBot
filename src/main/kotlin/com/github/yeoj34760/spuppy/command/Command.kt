package com.github.yeoj34760.spuppy.command

abstract class Command(val name: String, val aliases: List<String>, var isAllow: Boolean = true) {
    abstract suspend fun execute(event: CommandEvent)
}