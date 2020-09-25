package com.github.yeoj34760.spuppybot.commands.other

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.other.DiscordColor
import com.github.yeoj34760.spuppybot.settings
import net.dv8tion.jda.api.EmbedBuilder


@CommandSettings(name = "info")
object Info : Command() {

    override fun execute(event: CommandEvent) {


        val embed = EmbedBuilder()
                .setAuthor("SpuppyBot")
                .setDescription("스퍼피봇은 뮤직 기능을 제공하는 봇입니다.\n업데이트 소식이나 공지를 빠르게 확인을 하고 싶다면\n[Spuppy Official Server](https://discord.gg/rqpVtak)에 놀러와주세요.")
                .addField("서버 수", event.jda.guilds.size.toString(), true)
                .addField("라이센스", "[GPLv3](https://github.com/yeoj34760/SpuppyBot/blob/master/LICENSE)", true)
                .addField("오픈소스", "[GITHUB](https://github.com/yeoj34760/SpuppyBot)", true)
                .addField("명령어", "[CLICK](http://spuppy.ml/spuppybot/help/)", true)
                .addField("버전", settings.version, true)
                .addField("개발자", event.jda.retrieveUserById(settings.ownerId).complete().asTag, true)
                .setColor(DiscordColor.BLUE)
                .build()


        event.channel.sendMessage(embed).queue()
    }
}