package com.github.yeoj34760.spuppybot.command

import net.dv8tion.jda.api.hooks.ListenerAdapter

 abstract class Command(val name: CommandInfoName) {
    abstract fun execute(event: CommandEvent)
}