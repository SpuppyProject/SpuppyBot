package com.github.yeoj34760.spuppy.bot

import com.github.yeoj34760.spuppy.bot.commands.Ping
import com.github.yeoj34760.spuppy.command.CommandManager
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import java.io.File


object Bot {
private val botInfo = Json.decodeFromString<BotInfo>(File("settings.json").readText())
    @JvmStatic
     fun main(args: Array<String>) {
        val cmdManager = CommandManager(listOf(Ping), botInfo.prefix)
        JDABuilder.createDefault(botInfo.token).addEventListeners(cmdManager).build()
    }
}