package com.github.yeoj34760.rpg.command

import com.github.yeoj34760.rpg.rpg
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings


@CommandSettings(name = "dungeonlist", aliases = ["던전들"])
object DungeonList : Command() {
    override fun execute(event: CommandEvent) {
        val stringBuffer = StringBuffer()
        rpg.dungeonList.forEach {
            stringBuffer.append("**${it.name}**\n")
            stringBuffer.append("출현몬스터: ")
            stringBuffer.append(it.monsters.joinToString(separator = ", "))
            stringBuffer.append("\n드랍되는 무기: ")
            stringBuffer.append(it.dropWeaponList.joinToString(separator = ", "))
        }

        event.channel.sendMessage(stringBuffer.toString()).complete()
    }
}