package com.github.yeoj34760.spuppy.command

abstract class FilterCommand {
    abstract fun execute(event: CommandEvent) : Boolean
}