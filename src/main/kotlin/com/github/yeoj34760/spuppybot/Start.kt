package com.github.yeoj34760.spuppybot

import com.github.yeoj34760.spuppybot.Settings.TOKEN
import com.github.yeoj34760.spuppybot.command.CommandClient
import com.github.yeoj34760.spuppybot.commands.box.*
import com.github.yeoj34760.spuppybot.commands.music.*
import com.github.yeoj34760.spuppybot.commands.music.List
import com.github.yeoj34760.spuppybot.music.LeaveAutoListener
import com.github.yeoj34760.spuppybot.other.GuildAutoDeleteListener
import com.jagrosh.jdautilities.commons.waiter.EventWaiter
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager
import net.dv8tion.jda.api.JDABuilder


val playerManager = DefaultAudioPlayerManager()
val waiter = EventWaiter()

fun main(args: Array<String>) {
    Class.forName("org.mariadb.jdbc.Driver")
    //플레이어매니저 설정
    playerManager.registerSourceManager(YoutubeAudioSourceManager())
    AudioSourceManagers.registerRemoteSources(playerManager)
    CommandClient.addCommands(Connect,
            Disconnect,
            Info,
            List,
            Loop,
            NowPlay,
            Pause,
            Play,
            Remove,
            Search,
            Skip,
            Speed,
            Stop,
            Volume,
            AddBox,
            CopyAllBox,
            ListBox,
            MoveBox,
            RemoveAllBox,
            RemoveBox,
    Box,
    Shuffle)
    JDABuilder
            .createDefault(TOKEN)
            //        .setAudioSendFactory(NativeAudioSendFactory())
            .addEventListeners(
                    CommandClient,
                    waiter,
                    LeaveAutoListener,
                    GuildAutoDeleteListener
            )
            .build()
}
