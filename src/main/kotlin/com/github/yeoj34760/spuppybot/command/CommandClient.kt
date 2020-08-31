package com.github.yeoj34760.spuppybot.command

import com.github.yeoj34760.spuppybot.Settings
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

object CommandClient : ListenerAdapter() {
    private val commands = mutableMapOf<CommandInfoName, Command>()

    fun addCommands(vararg _commands: Command) {
        _commands.iterator().forEach { addCommand(it) }
    }

    fun addCommand(command: Command) {
        commands[command.name] = command
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {

        val message = event.message.contentRaw

//        Commands().forEach {
//            if (message.startsWith(Settings.PREFIX + it.command+ " ")) {
//                val commandEvent: CommandEvent = CommandEvent(event, Settings.PREFIX + it.command+ " ")
//                commands[check(it.name)]!!.execute(commandEvent)
//            }
//        }
        val commandInfo = Commands[message]
        if (commandInfo != null) {
            val commandEvent: CommandEvent = CommandEvent(event, Settings.PREFIX + commandInfo.command)
            commands[check(commandInfo.name)]!!.execute(commandEvent)
        }

    }

    private fun check(name: String): CommandInfoName {
        CommandInfoName.values().forEach {
            if (name == it.string)
                return it
        }
        return throw Exception("알 수 없는 에러")
    }


}