package com.github.yeoj34760.spuppy.command

import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

object ReactionEvent : ListenerAdapter() {
    private data class EventId(val emoji: String, val messageId: Long)
    private val eventMap: MutableMap<EventId, (e: MessageReactionAddEvent) -> Boolean> = mutableMapOf()

    fun add(emoji: String, message: Message, function: (e: MessageReactionAddEvent) -> Boolean) {
        eventMap[EventId(emoji, message.idLong)] = function
    }

    override fun onMessageReactionAdd(event: MessageReactionAddEvent) {
        eventMap.forEach { (eventId, function) ->
            if (eventId.emoji == event.reactionEmote.emoji && event.messageIdLong == eventId.messageId && function(event)) {
                eventMap.remove(eventId)
            }
        }
    }
}