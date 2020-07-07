package com.github.yeoj34760.spuppybot

import com.github.yeoj34760.spuppybot.commands.*
import com.jagrosh.jdautilities.command.CommandClientBuilder
import com.jagrosh.jdautilities.commons.waiter.EventWaiter
import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity

val token = ""
val ownerId = ""

val playerManager = DefaultAudioPlayerManager()
val waiter = EventWaiter()
fun main() {
    //플레이어매니저 설정
    playerManager.registerSourceManager(YoutubeAudioSourceManager())
    AudioSourceManagers.registerRemoteSources(playerManager)
    val commandClient = CommandClientBuilder()
            .setPrefix("?")
            .addCommands(Ping,
                    Play,
                    Stop,
                    Pause,
                    Skip,
                    Volume,
                    List,
                    Search,
                    Connect)
            .setOwnerId(ownerId)
            .setHelpConsumer { }
            .setActivity(Activity.playing("fuck"))
            .build()

    JDABuilder
            .createDefault(token)
            .setAudioSendFactory(NativeAudioSendFactory())
            .addEventListeners(commandClient, waiter)
            .build()

}