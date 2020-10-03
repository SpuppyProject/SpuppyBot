package com.github.yeoj34760.rpg.command

import com.github.yeoj34760.rpg.rpg
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.DiscordColor
import net.dv8tion.jda.api.EmbedBuilder


@CommandSettings(name = "dungeonlist", aliases = ["던전들"])
object DungeonList : Command() {
    override fun execute(event: CommandEvent) {
        val stringBuffer = StringBuffer()
        rpg.dungeonList.forEach {
            stringBuffer.append("**${it.name}**")
        }

        val embed = EmbedBuilder().setColor(DiscordColor.MINT)
                .setTitle("전설속 던전들")
                .setDescription(stringBuffer.toString())
                .build()
        event.channel.sendMessage(embed).complete()
    }
}