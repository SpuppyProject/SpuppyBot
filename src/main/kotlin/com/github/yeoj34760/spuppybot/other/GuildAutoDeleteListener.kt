package com.github.yeoj34760.spuppybot.other

import com.github.yeoj34760.spuppybot.sql.SpuppyDBController
import com.github.yeoj34760.spuppybot.sql.spuppydb.GuildDBController
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

object GuildAutoDeleteListener : ListenerAdapter() {
    override fun onGuildLeave(event: GuildLeaveEvent) {
        if (!GuildDBController.checkGuild(event.guild.idLong))
            GuildDBController.delGuild(event.guild.idLong)
    }
}