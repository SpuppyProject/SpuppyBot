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
        if (GuildManager[event.guild.idLong] == null || !GuildManager[event.guild.idLong]!!.isPlayed()) {
            event.reply("재생 중이지 않네요")
            return
        }

        val number: Double? = event.args.toDoubleOrNull()
        when (number) {
            null -> event.reply("올바르지 않은 값이네요")
            in 0.1..5.0 -> {
                GuildManager[event.guild.idLong]!!.speed(number)
                event.reply("`$number` 속도로 설정했습니다.")
            }
            else -> event.reply("값을 `0.1 ~ 5`로 지정해주세요")
        }
    }

}