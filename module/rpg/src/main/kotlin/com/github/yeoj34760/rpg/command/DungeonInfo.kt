package com.github.yeoj34760.rpg.command

import com.github.yeoj34760.rpg.rpg
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.DiscordColor
import net.dv8tion.jda.api.EmbedBuilder

@CommandSettings(name = "dungeoninfo", aliases = ["던전정보", "던전"])
object DungeonInfo : Command() {
    override fun execute(event: CommandEvent) {
        val dungeon = rpg.dungeonList.find { it.name == event.content }
        if (event.content.isEmpty()) { event.channel.sendMessage("던전 이름을 써주세요!").complete(); return }
        if (dungeon == null) { event.channel.sendMessage("해당 이름을 가진 던전을 찾을 수 없어요!").complete(); return }

        val embed = EmbedBuilder().setTitle(dungeon.name)
                .addField("출현 몬스터들", dungeon.monsters.joinToString(separator = "\n") { "。${it}" }, true)
                .addField("드랍되는 무기들", dungeon.dropWeaponList.joinToString(separator = "\n") { "。${it}" }, true)
                .setColor(DiscordColor.BLUE)
                .build()

        event.channel.sendMessage(embed).complete()

    }
}