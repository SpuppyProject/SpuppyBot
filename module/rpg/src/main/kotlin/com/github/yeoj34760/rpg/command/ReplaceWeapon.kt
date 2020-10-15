package com.github.yeoj34760.rpg.command

import com.github.yeoj34760.rpg.player
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings


@CommandSettings(name = "ReplaceWeapon")
object ReplaceWeapon : Command() {
    override fun execute(event: CommandEvent) {
        val player = event.author.player()
        val playerWeapon = player.weaponList.firstOrNull { it.name == event.content }

        if (playerWeapon == null) {
            event.channel.sendMessage("해당 무기를 가지고 있지 않아요").complete(); return
        }

        player.replaceWeapon(playerWeapon)
        event.channel.sendMessage("${playerWeapon.name}로 교체했어요!").queue()
    }
}