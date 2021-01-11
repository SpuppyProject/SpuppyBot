package com.github.yeoj34760.spuppy.bot

import com.github.yeoj34760.spuppy.bot.commands.Ping
import com.github.yeoj34760.spuppy.bot.commands.Test
import com.github.yeoj34760.spuppy.bot.commands.music.*
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

        //load SourceManagers
        playerManager.registerSourceManager(YoutubeAudioSourceManager())
        playerManager.registerSourceManager(TwitchStreamAudioSourceManager())
        AudioSourceManagers.registerRemoteSources(playerManager)
    }

    private fun loadCommands() {
        val element = Json.parseToJsonElement(File("commands.json").readText())
        //key - command name, value - aliases.
        val temp = mutableMapOf<String, List<String>>()

        element.jsonObject["commands"]!!.jsonArray.forEach { command ->
            val name = command.jsonObject["name"]!!.jsonPrimitive.content
            val aliasesList: MutableList<String> = mutableListOf()

            //read aliases of the command but add this in aliasesList.
            command.jsonObject["command"]!!.jsonArray.forEach { aliasesList.add(it.jsonPrimitive.content) }
            temp[name] = aliasesList
        }
        commands = temp
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val cmdManager = CommandManager(
            listOf(
                Ping,
                Play,
                Test,
                Stop,
                Skip,
                NowPlay,
                com.github.yeoj34760.spuppy.bot.commands.music.List,
                Remove
            ), info.prefix
        )
        JDABuilder.createDefault(info.token).addEventListeners(cmdManager).build()
    }
}