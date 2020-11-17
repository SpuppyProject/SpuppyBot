package com.github.yeoj34760.spuppybot.money.command.money

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.DiscordColor
import com.github.yeoj34760.spuppybot.db.UserDB
import com.github.yeoj34760.spuppybot.db.user.info
import net.dv8tion.jda.api.EmbedBuilder
import java.math.BigInteger
import java.util.*
import kotlin.random.Random

@CommandSettings(name = "receivemoney")
object ReceiveMoney : Command() {
    override fun execute(event: CommandEvent) {
        val userInfo = event.author.info()
        val timer = userInfo.receiveMoney.toDate()

        if (Date().time < timer.time) {
            event.channel.sendMessage("${(timer.time - Date().time) / 1000}초뒤에 다시시도해보세요").queue()
            return
        }
        val money = BigInteger(Random.nextInt(4000, 10000).toString())
        val now = userInfo.money.add(money)
         UserDB(event.author.idLong).moneyUpdate(now)
        val embed = EmbedBuilder().setColor(DiscordColor.ORANGE).setAuthor(event.author.asTag, null, event.author.avatarUrl
                ?: event.author.defaultAvatarUrl)
                .setTitle("처리 됨!")
                .setDescription("${money}원을 받았어요!")
                .setFooter("현재 돈 : $now")
                .build()

        event.channel.sendMessage(embed).queue()
        UserDB(event.author.idLong).receiveMoneyUpdate()
    }

}