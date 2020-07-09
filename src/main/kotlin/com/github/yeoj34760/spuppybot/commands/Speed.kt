package com.github.yeoj34760.spuppybot.commands

import com.github.yeoj34760.spuppybot.music.GuildManager
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent


/**
 * 스피드를 조절해줍니다.
 */
object Speed : Command() {
    init {
        super.name = "speed"
        super.aliases = arrayOf("speed", "스피드", "네", "sp", "넫ㄷㅇ")
    }

    override fun execute(event: CommandEvent) {
        if (!GuildManager.get(event.guild.idLong).isPlayed()) {
            event.reply("재생 중이지 않습니다.")
            return
        }

        val number: Double? = event.args.toDoubleOrNull()
        when (number) {
            null -> event.reply("올바르지 않은 값입니다.")
            in 0.1..5.0 -> {
                GuildManager.get(event.guild.idLong).speed(number)
                event.reply("적용되었습니다. 현재 속도 -> $number")
            }
            else -> event.reply("값을 `0.1 ~ 5`로 지정해주세요")
        }
    }

}