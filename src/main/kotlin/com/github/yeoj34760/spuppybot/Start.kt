package com.github.yeoj34760.spuppybot

import com.github.yeoj34760.spuppybot.music.LeaveAutoListener
import com.github.yeoj34760.spuppybot.music.command.*
import com.github.yeoj34760.spuppybot.music.command.List
import com.github.yeoj34760.spuppy.command.CommandClient
import com.github.yeoj34760.spuppy.command.CommandClientBuilder
import com.github.yeoj34760.spuppy.command.CommandDatabase
import com.github.yeoj34760.spuppybot.box.*
import com.github.yeoj34760.spuppybot.commands.game.gamble.Gamble
import com.github.yeoj34760.spuppybot.commands.game.gamble.GambleAll
import com.github.yeoj34760.spuppybot.commands.game.gamble.GambleInfo
import com.github.yeoj34760.spuppybot.commands.game.item.MyItem
import com.github.yeoj34760.spuppybot.commands.game.market.BuyMarket
import com.github.yeoj34760.spuppybot.commands.game.market.Market
import com.github.yeoj34760.spuppybot.commands.game.market.RefundMarket
import com.github.yeoj34760.spuppybot.commands.game.money.Money
import com.github.yeoj34760.spuppybot.commands.game.money.ReceiveMoney
import com.github.yeoj34760.spuppybot.commands.other.Agree
import com.github.yeoj34760.spuppybot.commands.other.Cancel
import com.github.yeoj34760.spuppybot.commands.other.Info
import com.github.yeoj34760.spuppybot.commands.other.Ping
import com.github.yeoj34760.spuppybot.other.FilterCommandImpl
import com.github.yeoj34760.spuppybot.other.GuildAutoDeleteListener
import com.github.yeoj34760.spuppybot.db.CommandDBController
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager
import net.dv8tion.jda.api.JDABuilder
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timer
import kotlin.random.Random


var nowGamblingProbability: Int = 0
var updateGamblingProbability: Date = Date()

const val TIMER = 5


fun main() {
    timer(period = 60 * TIMER.toLong() * 1000) {
        nowGamblingProbability = Random.nextInt(40, 65)
        val cal = Calendar.getInstance()
        cal.add(Calendar.MINUTE, TIMER)
        updateGamblingProbability = cal.time
    }

    Class.forName("org.mariadb.jdbc.Driver")
    //플레이어매니저 설정
    playerManager.registerSourceManager(YoutubeAudioSourceManager())
    AudioSourceManagers.registerRemoteSources(playerManager)


    var commandDatabase: CommandDatabase = CommandDatabase(ArrayList(CommandDBController.fromCommands()))
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
