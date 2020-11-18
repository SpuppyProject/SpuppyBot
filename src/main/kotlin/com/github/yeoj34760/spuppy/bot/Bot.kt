package com.github.yeoj34760.spuppy.bot

import com.github.yeoj34760.spuppy.bot.commands.Ping
import com.github.yeoj34760.spuppy.bot.commands.music.Play
import com.github.yeoj34760.spuppy.bot.language.Language
import com.github.yeoj34760.spuppy.bot.language.Languages
import com.github.yeoj34760.spuppy.command.CommandManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import net.dv8tion.jda.api.JDABuilder
import java.io.File


object Bot {
    val info = Json.decodeFromString<BotInfo>(File("settings.json").readText())
    val playerManager = DefaultAudioPlayerManager()
    lateinit var commands: Map<String, List<String>>
        private set

    init {
        loadCommands()
        playerManager.registerSourceManager(YoutubeAudioSourceManager())
        playerManager.registerSourceManager(TwitchStreamAudioSourceManager())
        AudioSourceManagers.registerRemoteSources(playerManager)
    }

    private fun loadCommands() {
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
        val cmdManager = CommandManager(listOf(Ping, Play), info.prefix)
        JDABuilder.createDefault(info.token).addEventListeners(cmdManager).build()
    }
}