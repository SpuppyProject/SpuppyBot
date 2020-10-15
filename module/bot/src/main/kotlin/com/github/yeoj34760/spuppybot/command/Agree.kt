package com.github.yeoj34760.spuppybot.command

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.DiscordColor
import com.github.yeoj34760.spuppybot.db.UserDB
import com.github.yeoj34760.spuppybot.waiter
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent
import net.dv8tion.jda.api.events.message.MessageDeleteEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.util.concurrent.TimeUnit


//해당 유저가 동의 확인하는 중인지 체크하기위한 변수
// key - UserId
val agreeUser = mutableMapOf<Long, Message>()

@CommandSettings(name = "agree", aliases = ["가입"])
object Agree : Command() {
    val text = """
        가입하시기 전에 아래에 있는 
        이용약관, 개인정보처리방침을 읽어주시고
        동의하신다면 ⭕ 이모티콘을 눌러해주세요.
    """.trimIndent()

    override fun execute(event: CommandEvent) {
        try {
            val userDB = UserDB(event.author.idLong)

            if (userDB.check()) {
                event.channel.sendMessage("이미 가입되어 있네요!").queue()
                return
            }

            if (agreeUser.containsKey(event.author.idLong)) {
                val channel = agreeUser[event.author.idLong]!!.channel
                when (event.channel.id == channel.id) {
                    true -> event.channel.sendMessage("위에 올려져있는 메세지를 확인해주세요!").queue()
                    false -> event.channel.sendMessage("`${channel.name}`에 있는 메세지를 확인해주세요!").queue()
                }
                return
            }

            val embed = EmbedBuilder().setAuthor("SpuppyBot").setDescription(text)
                    .setColor(DiscordColor.YELLOW)
                    .addField("이용약관", "[확인](http://spuppy.ml/tos/)", true)
                    .addField("개인정보처리방침", "[확인](http://spuppy.ml/privacy_policy/)", true)
                    .build()

            val message = event.channel.sendMessage(embed).complete()
            agreeUser[event.author.idLong] = message
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
                        userDB.create()
                        event.channel.sendMessage("가입되었어요! 환영해요!").complete()
                    }
                    "❌" -> event.channel.sendMessage("취소되었어요, 만약 가입할 마음이 생긴다면 다시 불러와주세요!").complete()
                }

            }, 1, TimeUnit.MINUTES) {
                if (!agreeUser.containsKey(event.author.idLong))
                    return@waitForEvent
                agreeUser.remove(event.author.idLong)
                event.channel.sendMessage("1분 지났어요, 만약 깜빡했다면 다시 불러와주세요!").complete()
            }
        } catch (e: Exception) {
            e.stackTrace
            agreeUser.remove(event.author.idLong)
            event.channel.sendMessage("죄송해요.. 알 수 없는 오류가 발생했어요..").complete()
        }
    }
}

object AgreeMessageDelete : ListenerAdapter() {
    override fun onMessageDelete(event: MessageDeleteEvent) = agreeUser.forEach { (userId, message) -> if (message.idLong == event.messageIdLong) agreeUser.remove(userId) }
    override fun onTextChannelDelete(event: TextChannelDeleteEvent) = agreeUser.forEach { (userId, message) -> if (message.channel.id == event.channel.id) agreeUser.remove(userId) }
}