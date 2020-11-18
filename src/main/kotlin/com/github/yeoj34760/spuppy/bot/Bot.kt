package com.github.yeoj34760.spuppy.bot

import com.github.yeoj34760.spuppy.bot.commands.Ping
import com.github.yeoj34760.spuppy.bot.commands.music.Play
import com.github.yeoj34760.spuppy.command.CommandManager
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import net.dv8tion.jda.api.JDABuilder
import java.io.File


object Bot {
    val info = Json.decodeFromString<BotInfo>(File("settings.json").readText())
    val commands: Map<String, List<String>>/* = Json.decodeFromString(File("commands.json").readText())*/
    init {
        val element = Json.parseToJsonElement(File("commands.json").readText())
        val temp = mutableMapOf<String, List<String>>()
        element.jsonObject["commands"]!!.jsonArray.forEach {
            val name = it.jsonObject["name"]!!.jsonPrimitive!!.content
            val alias: MutableList<String> = mutableListOf()
            it.jsonObject["command"]!!.jsonArray.forEach { alias.add(it.jsonPrimitive!!.content) }
            temp[name] = alias
        }
        commands = temp
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println(info)
        println(commands)
        val cmdManager = CommandManager(listOf(Ping, Play), info.prefix)
        JDABuilder.createDefault(info.token).addEventListeners(cmdManager).build()
    }
}