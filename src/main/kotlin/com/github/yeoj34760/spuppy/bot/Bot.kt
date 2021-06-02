package com.github.yeoj34760.spuppy.bot

import com.charleskorn.kaml.Yaml
import com.github.yeoj34760.spuppy.bot.commands.*
import com.github.yeoj34760.spuppy.bot.commands.gamble.AllIn
import com.github.yeoj34760.spuppy.bot.commands.gamble.Gamble
import com.github.yeoj34760.spuppy.bot.commands.gamble.Half
import com.github.yeoj34760.spuppy.bot.commands.money.GiveMoney
import com.github.yeoj34760.spuppy.bot.commands.money.Money
import com.github.yeoj34760.spuppy.bot.commands.money.SetMoney
import com.github.yeoj34760.spuppy.bot.commands.music.*
import com.github.yeoj34760.spuppy.command.CommandManager
import com.github.yeoj34760.spuppy.command.ReactionEvent
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import net.dv8tion.jda.api.JDABuilder
import java.io.File

object Bot {
   val info = Yaml.default.decodeFromString(BotInfo.serializer(), File("./config.yml").readText())
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
                List,
                Remove,
                Help,
                About,
                Money,
                GiveMoney,
                Gamble,
                Half,
                AllIn,
                SetMoney,
                    Register
            ), info.prefix
        )
     JDABuilder.createDefault(info.token).addEventListeners(cmdManager, ReactionEvent).build()
    }
}