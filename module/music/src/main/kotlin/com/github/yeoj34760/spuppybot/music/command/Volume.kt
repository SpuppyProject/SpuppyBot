package com.github.yeoj34760.spuppybot.music.command


import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.db.GuildDB
import com.github.yeoj34760.spuppybot.music.GuildManager
import com.github.yeoj34760.spuppybot.music.GuildManager.playerControls

/**
 * 볼륨 기능을 제공하기 위한 오브젝트입니다.
 */
@CommandSettings(name = "volume")
object Volume : Command() {

    override fun execute(event: CommandEvent) {
        val id = event.guild.idLong
        val audioManager = event.guild.audioManager
        GuildManager.check(audioManager, id)

        if (event.args.isEmpty()) {
            event.channel.sendMessage("현재 ${GuildDB.volume(id)} 볼륨으로 지정되어 있어요!").complete()
            return
        }

        if (event.args[0].toIntOrNull() == null) {
            event.channel.sendMessage("숫자를 제대로 써주세요.").queue()
        }

        when (val argsInt: Int = event.args[0].toInt()) {
            in 0..100 -> {
                playerControls[id]?.volume(argsInt)
                GuildDB.volume(id, argsInt)
                event.channel.sendMessage("`$argsInt`(으)로 볼륨 조정했어요!").queue()
            }
            else -> event.channel.sendMessage("숫자를 `1 ~ 100`으로 지정해주세요!").queue()
        }
    }
}