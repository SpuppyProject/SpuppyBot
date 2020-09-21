package com.github.yeoj34760.spuppybot

import com.github.yeoj34760.spuppy.command.CommandClient
import com.github.yeoj34760.spuppy.command.CommandClientBuilder
import com.github.yeoj34760.spuppy.command.CommandDatabase
import com.github.yeoj34760.spuppybot.commands.other.Agree
import com.github.yeoj34760.spuppybot.commands.other.Cancel
import com.github.yeoj34760.spuppybot.commands.other.Info
import com.github.yeoj34760.spuppybot.commands.other.Ping
import com.github.yeoj34760.spuppybot.commands.box.*
import com.github.yeoj34760.spuppybot.commands.game.money.Money
import com.github.yeoj34760.spuppybot.commands.game.money.ReceiveMoney
import com.github.yeoj34760.spuppybot.commands.music.*
import com.github.yeoj34760.spuppybot.commands.music.List
import com.github.yeoj34760.spuppybot.music.LeaveAutoListener
import com.github.yeoj34760.spuppybot.other.FilterCommandImpl
import com.github.yeoj34760.spuppybot.other.GuildAutoDeleteListener
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController
import com.google.gson.Gson
import com.jagrosh.jdautilities.commons.waiter.EventWaiter
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager
import net.dv8tion.jda.api.JDABuilder
import java.io.File


val playerManager = DefaultAudioPlayerManager()
val waiter = EventWaiter()
val settings: Settings = Gson().fromJson(File("settings.json").readText(), Settings::class.java)

fun main(args: Array<String>) {

    Class.forName("org.mariadb.jdbc.Driver")
    //플레이어매니저 설정
    playerManager.registerSourceManager(YoutubeAudioSourceManager())
    AudioSourceManagers.registerRemoteSources(playerManager)


    var commandDatabase: CommandDatabase = CommandDatabase(ArrayList(SpuppyDBController.fromCommands()))
    var commandClient: CommandClient = CommandClientBuilder().setPrefix(settings.prefix)
            .setCommandDatabase(commandDatabase)
            .addFilterCommand(FilterCommandImpl, "agree", "cancel", "info", "ping")
            .addCommands(
                    Connect,
                    Disconnect,
                    Info,
                    List,
                    Loop,
                    NowPlay,
                    Pause,
                    Play,
                    Remove,
                    Search,
                    Shuffle,
                    Skip,
                    Speed,
                    Stop,
                    Volume,
                    AddBox,
                    Box,
                    CopyAllBox,
                    ListBox,
                    MoveBox,
                    RemoveAllBox,
                    RemoveBox,
                    CopyBox,
                    Agree,
                    Cancel,
                    Ping,
                    Money,
                    ReceiveMoney
            ).build()
    JDABuilder
            .createDefault(settings.token)
            //        .setAudioSendFactory(NativeAudioSendFactory())
            .addEventListeners(
                    commandClient,
                    waiter,
                    LeaveAutoListener,
                    GuildAutoDeleteListener
            )
            .build()
}
