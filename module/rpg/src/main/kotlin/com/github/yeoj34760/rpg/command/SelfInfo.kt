package com.github.yeoj34760.rpg.command

import com.github.yeoj34760.rpg.Player
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.DiscordColor
import net.dv8tion.jda.api.EmbedBuilder

@CommandSettings(name = "selfinfo", aliases = ["내정보"])
object SelfInfo : Command() {
    override fun execute(event: CommandEvent) {

        val player = Player.create(event.author.idLong)
        val embed = EmbedBuilder().setColor(DiscordColor.GREEN)
                .setTitle("`${event.author.name}`님의 정보")
                .addField("사용중인 무기", player.weapon.name, true)
                .addField("레벨", player.level.toString(), true)
                .addField("몬스터 킬 수", player.monsterKill.toString(), true)
                .addField("무기 수", player.weaponList.map { it.count }.reduce { i, j -> i + j }.toString(), true)
                .build()
        event.channel.sendMessage(embed).complete()
    }
}