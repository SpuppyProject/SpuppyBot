package com.github.yeoj34760.spuppybot

import com.github.yeoj34760.spuppybot.Settings.OWNER_ID
import com.github.yeoj34760.spuppybot.Settings.PREFIX
import com.github.yeoj34760.spuppybot.Settings.TOKEN
import com.github.yeoj34760.spuppybot.commands.*
import com.github.yeoj34760.spuppybot.music.LeaveAutoListener
import com.jagrosh.jdautilities.command.CommandClientBuilder
import com.jagrosh.jdautilities.commons.waiter.EventWaiter
import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity




val playerManager = DefaultAudioPlayerManager()
val waiter = EventWaiter()

fun main(args: Array<String>) {
    //플레이어매니저 설정
    playerManager.registerSourceManager(YoutubeAudioSourceManager())
    AudioSourceManagers.registerRemoteSources(playerManager)
    val commandClient = CommandClientBuilder()
            .setPrefix(PREFIX)
            .addCommands(
                    Ping,
                    Play,
                    Stop,
                    Pause,
                    Skip,
                    Volume,
                    List,
                    Search,
                    Connect,
                    Speed,
                    Disconnect,
                    NowPlay,
                    Info,
                    Remove,
                    Loop
            )
            .setOwnerId(OWNER_ID)
            .setHelpConsumer { }
            .setActivity(Activity.playing("?info"))
            .build()

    JDABuilder
            .createDefault(TOKEN)
            .setAudioSendFactory(NativeAudioSendFactory())
            .addEventListeners(commandClient, waiter, LeaveAutoListener)
            .build()
}
