package com.github.yeoj34760.spuppybot.commands.other

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.DiscordColor
import com.github.yeoj34760.spuppybot.db.UserDBController
import com.github.yeoj34760.spuppybot.waiter
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import java.util.concurrent.TimeUnit

@CommandSettings(name = "agree", aliases = ["가입"])
object Agree : Command() {
    val text = """
        가입하시기 전에 아래에 있는 
        이용약관, 개인정보처리방침을 읽어주시고
        동의하신다면 ⭕ 이모티콘을 눌러해주세요.
    """.trimIndent()

    override fun execute(event: CommandEvent) {
        if (UserDBController.checkUser(event.author.idLong)) {
            event.channel.sendMessage("이미 가입되어 있네요!").queue()
            return
        }

        val embed = EmbedBuilder().setAuthor("SpuppyBot").setDescription(text)
                .setColor(DiscordColor.YELLOW)
                .addField("이용약관", "[확인](http://spuppy.ml/tos/)", true)
                .addField("개인정보처리방침", "[확인](http://spuppy.ml/privacy_policy/)", true)
                .build()

        val message = event.channel.sendMessage(embed).complete()
        message.addReaction("⭕").complete()
        message.addReaction("❌").complete()


        waiter.waitForEvent(MessageReactionAddEvent::class.java, {
            it.messageId == message.id &&
                    event.author.id == it.userId &&
                    (it.reactionEmote.emoji == "⭕" ||
                            it.reactionEmote.emoji == "❌")
        }, {
            message.delete().complete()

            when (it.reactionEmote.emoji) {
                "⭕" -> {
                    UserDBController.addUser(event.author.idLong)
                    event.channel.sendMessage("가입되었어요! 환영해요!").complete()
                }
                "❌" -> event.channel.sendMessage("취소되었어요, 만약 가입할 마음이 생긴다면 다시 불러와주세요!").complete()
            }

        }, 1, TimeUnit.MINUTES) {
            event.channel.sendMessage("1분 지났어요. 만약 깜빡했다면 다시 불러와주세요").complete()
        }
    }
}