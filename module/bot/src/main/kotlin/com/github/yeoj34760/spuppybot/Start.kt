package com.github.yeoj34760.spuppybot

import com.github.yeoj34760.spuppy.command.CommandClient
import com.github.yeoj34760.spuppy.command.CommandClientBuilder
import com.github.yeoj34760.spuppy.command.CommandDatabase
import com.github.yeoj34760.spuppy.command.Commands
import com.github.yeoj34760.spuppybot.command.*
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
import com.github.yeoj34760.spuppybot.music.LeaveAutoListener
import com.github.yeoj34760.spuppybot.music.command.*
import com.github.yeoj34760.spuppybot.music.command.List
import com.github.yeoj34760.spuppybot.other.FilterCommandImpl
import com.github.yeoj34760.spuppybot.other.GuildAutoDeleteListener
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity

fun main() {
    GambleTimer.start()
    Class.forName("org.mariadb.jdbc.Driver")
    //playerManager set up
    playerManager.registerSourceManager(YoutubeAudioSourceManager())
    AudioSourceManagers.registerRemoteSources(playerManager)

    val commands = mutableListOf<Commands>()
    cmdJson.commands.forEach {
        commands.add(Commands(it.name, ArrayList(it.command)))
    }


    val commandDatabase = CommandDatabase(ArrayList(commands))
    val commandClient: CommandClient = CommandClientBuilder().setPrefix(settings.prefix)
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
//                    AddPlaylist,
//                    Box,
//                    CopyAllBox,
//                    ListBox,
//                    MoveBox,
//                    RemoveAllBox,
//                    RemoveBox,
//                    CopyBox,
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
                    GambleAll,
//                    WeaponHelp,
//                    WeaponList,
//                    DungeonList,
//                    SelfInfo,
//                    DungeonInfo,
//                    RpgStart,
//                    SelfWeapon,
//                    SelfWeaponList,
//                    ReplaceWeapon,
//                    CreatePlaylist
            ).build()

    JDABuilder
            .createDefault(settings.token)
            // this is not compatible with macOS
            //        .setAudioSendFactory(NativeAudioSendFactory())
            .addEventListeners(
                    commandClient,
                    waiter,
                    LeaveAutoListener,
                    GuildAutoDeleteListener,
                    AgreeMessageDelete
            )
            .setActivity(Activity.playing("?help | 개훈련"))
            .build()
}
