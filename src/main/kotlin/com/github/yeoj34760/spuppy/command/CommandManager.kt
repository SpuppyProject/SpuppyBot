package com.github.yeoj34760.spuppy.command

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class CommandManager(commands: List<Command>,
                     private val prefix: String,
                     private val filterCommands: List<FilterCommand> = listOf()
) : ListenerAdapter() {
    private val aliasMap: Map<String, Command>

    init {
        val tempMap = mutableMapOf<String, Command>()
        commands.forEach { cmd ->
            cmd.alias.forEach { alias -> tempMap[alias] = cmd }
        }
        aliasMap = tempMap.toSortedMap(compareBy<String> { it.length }.reversed())
    }

    override fun onMessageReceived(event: MessageReceivedEvent) = messageCheck(event)
    private fun messageCheck(event: MessageReceivedEvent) {
        aliasMap.forEach { (alias, cmd) ->
            val message = event.message.contentRaw
            if (message.startsWith(prefix + alias))
                firstBlankCheck(event, cmd, alias)
        }
    }

    private fun firstBlankCheck(event: MessageReceivedEvent, command: Command, alias: String) {
        val message = event.message.contentRaw
        val cmdLength = (prefix + alias).length

        if (message.length > cmdLength && message[cmdLength] != ' ')
            return

        filterCheck(event, command)
    }

    private fun filterCheck(event: MessageReceivedEvent, command: Command) {
        val commandEvent = CommandEvent(event.jda, event.responseNumber, event.message)
        if (filterCommands.isEmpty())
            command.execute(commandEvent)

        filterCommands.forEach {
            if (it.execute(commandEvent)) {
                command.execute(commandEvent); return@forEach
            }
        }
    }
}