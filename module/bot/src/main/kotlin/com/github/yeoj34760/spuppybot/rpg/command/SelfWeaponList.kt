package com.github.yeoj34760.spuppybot.rpg.command


import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.DiscordColor
import com.github.yeoj34760.spuppybot.rpg.player
import net.dv8tion.jda.api.EmbedBuilder
import java.text.SimpleDateFormat


@CommandSettings(name = "SelfWeaponList")
object SelfWeaponList : Command() {
    override fun execute(event: CommandEvent) {
        val player = event.author.player()
        val stringBuffer = StringBuffer()
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        player.weaponList.forEach {
            val time = format.format(it.time.toDate())
            stringBuffer.append("**${it.name}** `${it.count}개`\n。`${time}`\n\n")

        }
        val embed = EmbedBuilder().setTitle("내 무기들")
                .setDescription(stringBuffer.toString())
                .setColor(DiscordColor.BLUE)
                .build()

        event.channel.sendMessage(embed).complete()
    }
}