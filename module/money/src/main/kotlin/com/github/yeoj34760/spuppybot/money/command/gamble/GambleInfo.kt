package com.github.yeoj34760.spuppybot.money.command.gamble

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.DiscordColor
import com.github.yeoj34760.spuppybot.money.GambleTimer.nowGamblingProbability
import com.github.yeoj34760.spuppybot.money.GambleTimer.updateGamblingProbability
import net.dv8tion.jda.api.EmbedBuilder
import java.text.SimpleDateFormat
import java.util.*


@CommandSettings(name = "gambleinfo")
object GambleInfo : Command() {
    override fun execute(event: CommandEvent) {
        val embed = EmbedBuilder().setColor(DiscordColor.GREEN)
                .addField("현재 확률", "${nowGamblingProbability}%", true)
                .addField("확률 갱신까지", SimpleDateFormat("mm분 ss초").format(Date(updateGamblingProbability.time - Date().time)), true)
                .build()

        event.channel.sendMessage(embed).queue()
    }
}