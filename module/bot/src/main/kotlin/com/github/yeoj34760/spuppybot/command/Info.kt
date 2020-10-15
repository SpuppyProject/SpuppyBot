package com.github.yeoj34760.spuppybot.command

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.DiscordColor
import com.github.yeoj34760.spuppybot.settings
import net.dv8tion.jda.api.EmbedBuilder


@CommandSettings(name = "info")
object Info : Command() {

    private val text = """
        스퍼피봇은 뮤직 기능을 제공하는 봇이예요.
        업데이트 소식이나 공지를 빠르게 확인을 하고 싶다면
        [스퍼피 공식 서버](https://discord.gg/rqpVtak)에 놀러와주세요.
    """.trimIndent()

    override fun execute(event: CommandEvent) {
        val embed = EmbedBuilder()
                .setAuthor("SpuppyBot")
                .setDescription(text)
                .addField("서버 수", event.jda.guilds.size.toString(), true)
                .addField("라이센스", "[GPLv3](https://github.com/yeoj34760/SpuppyBot/blob/master/LICENSE)", true)
                .addField("오픈소스", "[GITHUB](https://github.com/yeoj34760/SpuppyBot)", true)
                .addField("명령어", "[CLICK](http://spuppy.ml/spuppybot/help/)", true)
                .addField("버전", settings.version, true)
                .addField("초대하기", "[CLICK](https://discord.com/api/oauth2/authorize?client_id=439755380785152000&permissions=0&scope=bot)", true)
                .addField("개발자", event.jda.retrieveUserById(settings.ownerId).complete().asTag, true)
                .setColor(DiscordColor.BLUE)
                .build()

        event.channel.sendMessage(embed).queue()
    }
}