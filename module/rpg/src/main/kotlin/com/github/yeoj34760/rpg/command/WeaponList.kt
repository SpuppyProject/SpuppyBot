package com.github.yeoj34760.rpg.command

import com.github.yeoj34760.rpg.rpg
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings


@CommandSettings(name="weaponlist", aliases = ["무기들"])
object WeaponList : Command() {
    override fun execute(event: CommandEvent) {
        val stringBuffer = StringBuffer()
        rpg.weaponList.forEach {
            stringBuffer.append("**${it.name}**\n")
            stringBuffer.append("타입: ${it.type.alias}\n")
            stringBuffer.append("레벨: ${it.level}\n")
//            stringBuffer.append("공격력: ${it.power}\n\n")
        }

        event.channel.sendMessage(stringBuffer.toString()).complete()
    }
}