package com.github.yeoj34760.spuppybot.music.command


import com.github.yeoj34760.spuppybot.music.GuildManager
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings

@CommandSettings(name = "shuffle")
object Shuffle : Command() {
    override fun execute(event: CommandEvent) {
        if (GuildManager.playerControls[event.guildIdLong] == null ||
                GuildManager.playerControls[event.guildIdLong]!!.count() < 2) {
            event.channel.sendMessage("최소 음악리스트에 2개이상 있어야합니다.").queue()
            return
        }

        GuildManager.playerControls[event.guildIdLong]!!.shuffled()
        event.channel.sendMessage("섞였습니다").queue()
    }
}