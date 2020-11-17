package com.github.yeoj34760.spuppybot.rpg.command

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.DiscordColor
import com.github.yeoj34760.spuppybot.rpg.weapon.WeaponType
import net.dv8tion.jda.api.EmbedBuilder

@CommandSettings(name = "무기도움말", aliases = ["무기도움말"])
object WeaponHelp : Command() {
    override fun execute(event: CommandEvent) {
        val stringBuffer = StringBuffer()
        stringBuffer.append("**무기 종류**\n")
        WeaponType.values().forEach {
            stringBuffer.append("${it.alias} - ${it.content}\n")
        }

        val embed = EmbedBuilder().setDescription(stringBuffer.toString()).setColor(DiscordColor.YELLOW).build()
        event.channel.sendMessage(embed).complete()
    }
}