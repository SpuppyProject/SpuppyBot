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

        Commands().forEach {
            if (message.startsWith(Settings.PREFIX + it.command)) {
                val commandEvent: CommandEvent = CommandEvent(event)
                commands[check(it.name)]!!.execute(commandEvent)
            }
        }

    }

    private fun check(name: String): CommandInfoName {
        CommandInfoName.values().forEach {
            if (name == it.fromString())
                return it
        }
        return throw Exception("알 수 없는 에러")
    }


}