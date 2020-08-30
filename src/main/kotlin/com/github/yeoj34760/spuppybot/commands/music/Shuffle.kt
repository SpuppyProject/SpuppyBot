package com.github.yeoj34760.spuppybot.commands.music

import com.github.yeoj34760.spuppybot.command.Command
import com.github.yeoj34760.spuppybot.command.CommandEvent
import com.github.yeoj34760.spuppybot.command.CommandInfoName
import com.github.yeoj34760.spuppybot.music.GuildManager
import com.github.yeoj34760.spuppybot.music.PlayerControl

object Shuffle : Command(CommandInfoName.SHUFFLE) {
    override fun execute(event: CommandEvent) {
        if (GuildManager.playerControls[event.guildIdLong] == null &&
                GuildManager.playerControls[event.guildIdLong]!!.count() < 2) {
            event.channel.sendMessage("최소 음악리스트에 2개이상 있어야합니다.").queue()
            return
        }

        event.playerControl?.shuffled()
        event.channel.sendMessage("섞였습니다").queue()
    }
}