package com.github.yeoj34760.spuppybot.command

import com.github.yeoj34760.spuppybot.Settings
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController
import kotlin.streams.toList

object Commands {
    var commandInfos: MutableList<CommandInfo> = SpuppyDBController.fromCommands()
    operator fun get(group: CommandInfoGroup): List<CommandInfo> = commandInfos.stream().filter { it.group == group.fromString() }.toList()
    operator fun get(name: CommandInfoName): List<CommandInfo> = commandInfos.stream().filter { it.name == name.fromString() }.toList()
    operator fun get(name: CommandInfoName, group: CommandInfoGroup): List<CommandInfo> = commandInfos.stream().filter { it.name == name.fromString() && it.group == group.fromString() }.toList()
    operator fun get(message: String): CommandInfo? {
        val result = commandInfos.stream().filter {message.startsWith(Settings.PREFIX + it.command)}.findFirst()
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