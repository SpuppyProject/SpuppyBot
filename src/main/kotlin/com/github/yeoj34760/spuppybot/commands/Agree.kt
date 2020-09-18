package com.github.yeoj34760.spuppybot.commands

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.Settings
import com.github.yeoj34760.spuppybot.other.DiscordColor
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController
import com.github.yeoj34760.spuppybot.waiter
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.util.concurrent.TimeUnit

@CommandSettings(name = "agree", aliases = ["가입"])
object Agree : Command() {
    override fun execute(event: CommandEvent) {
        if (SpuppyDBController.checkUser(event.author.idLong)) {
            event.channel.sendMessage("이미 가입되어 있네요!").queue()
            return
        }

        val embed = EmbedBuilder().setAuthor("SpuppyBot").setDescription("가입하시기 전에 아래에 있는 \n이용약관, 개인정보처리방침을 읽어주시고\n동의하신다면 `${Settings.PREFIX}동의` 입력해주세요.")
                .setColor(DiscordColor.YELLOW)
                .addField("이용약관", "[확인](http://spuppy.ml/tos/)", true)
                .addField("개인정보처리방침", "[확인](http://spuppy.ml/privacy_policy/)", true)
                .build()

        event.channel.sendMessage(embed).queue()

        waiter.waitForEvent(MessageReceivedEvent::class.java, { e ->
            e.author == event.author
                    && e.channel == event.channel
                    && e.message != event.message
        }, { e ->
            if (e.message.contentRaw == "${Settings.PREFIX}동의") {
                SpuppyDBController.addUser(e.author.idLong)
                event.channel.sendMessage("가입완료!").queue()
            }
            else
                return@waitForEvent
        }, 1, TimeUnit.MINUTES) {
            event.channel.sendMessage("시간 초과됨").queue()
        }
    }
}