package com.github.yeoj34760.spuppybot.commands


import com.github.yeoj34760.spuppybot.Settings.VERSION
import com.github.yeoj34760.spuppybot.other.DiscordColor
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.EmbedBuilder

object Info : Command() {
    init {
        name = "info"
        aliases = arrayOf("info", "ㅑㅜ래", "ㅑㅜ")
    }

    override fun execute(event: CommandEvent) {
        val embed = EmbedBuilder()
                .setAuthor("SpuppyBot")
                .setDescription("스퍼피봇은 뮤직 기능을 제공하는 봇입니다.")
                .addField("servers", event.jda.guilds.size.toString(), true)
                .addField("License", "[GPLv3](https://github.com/yeoj34760/SpuppyBot/blob/master/LICENSE)", true)
                .addField("open source", "[GITHUB](https://github.com/yeoj34760/SpuppyBot)", true)
                .addField("commands", "[CLICK](https://github.com/yeoj34760/SpuppyBot#%EB%AA%85%EB%A0%B9%EC%96%B4)", true)
                .addField("version", VERSION, true)
                .addField("developer", "ImperskyPenguin#1090", true)
                .setColor(DiscordColor.BLUE)
                .build()

        event.reply(embed)
    }
}