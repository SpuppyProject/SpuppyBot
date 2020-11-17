package com.github.yeoj34760.spuppybot.rpg.command

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.DiscordColor
import com.github.yeoj34760.spuppybot.rpg.player
import net.dv8tion.jda.api.EmbedBuilder
import java.text.SimpleDateFormat

@CommandSettings(name="selfweapon")
object SelfWeapon : Command() {
    override fun execute(event: CommandEvent) {
            val player = event.author.player()
//            val stringBuffer = StringBuffer()
//        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//            player.weaponList.forEach {
//                val time = format.format(it.time.toDate())
//                stringBuffer.append("**${it.name}** `${it.count}개`\n。`${time}`\n\n")
//
//            }
        val weapon = player.weapon
            val embed = EmbedBuilder()
                    .setAuthor(weapon.name, null, event.author.avatarUrl ?: event.author.defaultAvatarUrl)
//                    .addField("공격력", weapon.power.toString(), true)
                    .addField("레벨", weapon.level.toString(), true)
                    .addField("타입", weapon.type.alias, true)
                    .setFooter("${weapon.type.alias} - ${weapon.type.content}")
                    .setColor(DiscordColor.GREEN)
                    .build()


            event.channel.sendMessage(embed).complete()
    }
}