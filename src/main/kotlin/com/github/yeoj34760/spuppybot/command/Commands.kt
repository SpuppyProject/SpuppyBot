package com.github.yeoj34760.spuppybot.command

import com.github.yeoj34760.spuppybot.Settings
import com.github.yeoj34760.spuppybot.other.Util
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController
import java.util.*
import kotlin.streams.toList


object Commands {
    var commandInfos: MutableList<CommandInfo> = SpuppyDBController.fromCommands()
    operator fun get(group: CommandInfoGroup): List<CommandInfo> = commandInfos.stream().filter { it.group == group.fromString() }.toList()
    operator fun get(name: CommandInfoName): List<CommandInfo> = commandInfos.stream().filter { it.name == name.string }.toList()
    operator fun get(name: CommandInfoName, group: CommandInfoGroup): List<CommandInfo> = commandInfos.stream().filter { it.name == name.string && it.group == group.fromString() }.toList()
    operator fun get(message: String): CommandInfo? {
        var messageCountMax: Int = 0
        val messageCommand = Util.stringToArgs(message)
        println(messageCommand[0].count())
        val result = commandInfos.stream()
                .filter {
                 message.startsWith(Settings.PREFIX + it.command)
                         && messageCommand[0].count() <= (Settings.PREFIX + it.command).count()
                         && messageCommand[0] == (Settings.PREFIX + it.command).substring(0,messageCommand[0].count())
                }
                .max(Comparator.comparing{it.command.length})


        return if (result.isPresent)
            result.get()
        else
            null
    }
    operator fun invoke(): List<CommandInfo> { return commandInfos }

    fun reload() {
        commandInfos = SpuppyDBController.fromCommands()
    }
}