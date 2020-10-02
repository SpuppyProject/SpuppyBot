package com.github.yeoj34760.spuppybot.other

import com.github.yeoj34760.spuppybot.db.GuildDB
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

object GuildAutoDeleteListener : ListenerAdapter() {
    override fun onGuildLeave(event: GuildLeaveEvent) {
        GuildDB.remove(event.guild.idLong)
    }
}