package com.github.yeoj34760.spuppybot

import com.github.yeoj34760.spuppybot.music.LeaveAutoListener
import com.github.yeoj34760.spuppybot.music.command.*
import com.github.yeoj34760.spuppybot.music.command.List
import com.github.yeoj34760.spuppy.command.CommandClient
import com.github.yeoj34760.spuppy.command.CommandClientBuilder
import com.github.yeoj34760.spuppy.command.CommandDatabase
import com.github.yeoj34760.spuppybot.box.*
import com.github.yeoj34760.spuppybot.command.Agree
import com.github.yeoj34760.spuppybot.command.Cancel
import com.github.yeoj34760.spuppybot.command.Info
import com.github.yeoj34760.spuppybot.command.Ping
import com.github.yeoj34760.spuppybot.other.FilterCommandImpl
import com.github.yeoj34760.spuppybot.other.GuildAutoDeleteListener
import com.github.yeoj34760.spuppybot.db.CommandDBController
import com.github.yeoj34760.spuppybot.money.GambleTimer
import com.github.yeoj34760.spuppybot.money.command.gamble.Gamble
import com.github.yeoj34760.spuppybot.money.command.gamble.GambleAll
import com.github.yeoj34760.spuppybot.money.command.gamble.GambleInfo
import com.github.yeoj34760.spuppybot.money.command.item.MyItem
import com.github.yeoj34760.spuppybot.money.command.market.BuyMarket
import com.github.yeoj34760.spuppybot.money.command.market.Market
import com.github.yeoj34760.spuppybot.money.command.market.RefundMarket
import com.github.yeoj34760.spuppybot.money.command.money.Money
import com.github.yeoj34760.spuppybot.money.command.money.ReceiveMoney
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager
import net.dv8tion.jda.api.JDABuilder
import kotlin.collections.ArrayList

fun main() {
    GambleTimer.start()
    Class.forName("org.mariadb.jdbc.Driver")
    //플레이어매니저 설정
    playerManager.registerSourceManager(YoutubeAudioSourceManager())
    AudioSourceManagers.registerRemoteSources(playerManager)


    var commandDatabase: CommandDatabase = CommandDatabase(ArrayList(CommandDBController.fromCommands()))
    var commandClient: CommandClient = CommandClientBuilder().setPrefix(settings.prefix)
            .setCommandDatabase(commandDatabase)
            .addFilterCommand(FilterCommandImpl, "agree", "cancel", "info", "ping")
            .addFilterCommand { !it.author.isBot }
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
                    ReceiveMoney,
                    Market,
                    BuyMarket,
                    MyItem,
                    Gamble,
                    GambleInfo,
                    RefundMarket,
                    GambleAll
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
