package com.github.yeoj34760.spuppybot.commands.game.money

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.other.DiscordColor
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController
import com.github.yeoj34760.spuppybot.sql.spuppydb.ReceiveMoneyDBController
import com.github.yeoj34760.spuppybot.sql.spuppydb.UserMoneyDBController
import net.dv8tion.jda.api.EmbedBuilder
import java.math.BigInteger
import java.util.*
import kotlin.random.Random

@CommandSettings(name="receivemoney")
object ReceiveMoney : Command() {
    override fun execute(event: CommandEvent) {

        if(!UserMoneyDBController.checkMoneyUser(event.author.idLong))
            UserMoneyDBController.createMoneyUser(event.author.idLong)

        if (!ReceiveMoneyDBController.checkReceiveMoneyUser(event.author.idLong))
            ReceiveMoneyDBController.createReceiveMoneyUser(event.author.idLong)


        val timer = ReceiveMoneyDBController.receiveMoneyTimer(event.author.idLong)

        if (Date().time < timer)
        {
            event.channel.sendMessage("${ (timer - Date().time ) / 1000}초뒤에 다시시도해보세요").queue()
            return
        }
        val money = BigInteger(Random.nextInt(4000, 10000).toString())
       val nowMoney = UserMoneyDBController.addMoneyUser(event.author.idLong, money)
        val embed = EmbedBuilder().setColor(DiscordColor.ORANGE).setAuthor(event.author.asTag, null, event.author.avatarUrl ?: event.author.defaultAvatarUrl)
                .setTitle("처리 됨!")
                .setDescription("${money}원을 받았습니다.")
                .setFooter("현재 돈 : $nowMoney")
                .build()

        event.channel.sendMessage(embed).queue()
        ReceiveMoneyDBController.setupReceiveMoneyTimer(event.author.idLong)
    }

}