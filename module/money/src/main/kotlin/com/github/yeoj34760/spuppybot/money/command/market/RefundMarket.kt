package com.github.yeoj34760.spuppybot.money.command.market

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.DiscordColor
import com.github.yeoj34760.spuppybot.db.MarketItemDB
import com.github.yeoj34760.spuppybot.db.UserDB
import com.github.yeoj34760.spuppybot.db.user.info
import net.dv8tion.jda.api.EmbedBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigDecimal

@CommandSettings(name = "refundmarket")
object RefundMarket : Command() {
    private val logger: Logger = LoggerFactory.getLogger("환불 명령어")

    override fun execute(event: CommandEvent) {
        if (event.args.isEmpty()) {
            event.channel.sendMessage("아이템 이름을 적어주세요!").queue()
        }
        val regex = "(?<= )[0-9]+".toRegex()
        val temp = event.content.replace(regex, "").trim()
        val countFind = regex.find(event.content)

        val count = countFind?.value?.toInt() ?: 1
        var item = MarketItemDB.list().filter { it.name == temp }[0]

        if (item == null) {
            logger.info("아이템을 발견하지 못함")
            event.channel.sendMessage("해당 이름을 가진 아이템을 찾을 수 없어요").queue()
            return
        }
        val myItem = event.author.info.itemList.filter { it.name == temp }[0]

        if (myItem == null) {
            logger.info("[${event.author.idLong}] 유저가 해당 아이템을 가지고 있지 않음")
            event.channel.sendMessage("해당 아이템을 가지고 있지 않아요!").queue()
            return
        } else {
            if (myItem.count < count) {
                logger.info("[${event.author.idLong}] 유저가 가지고 있는 아이템 갯수보다 높은 값을 입력함")
                event.channel.sendMessage("자신이 가지고 있는 아이템 수보다 많이 팔 수 없어요").queue()
                return
            }
        }
        val userDB = UserDB(event.author.idLong)
        item.add(count)
        logger.info("[${event.author.idLong}] 해당 아이템 갯수 값을 뻄")
        userDB.itemMinus(item.name, count)
        var money: BigDecimal = BigDecimal((item.price * count).toString())
        money = money.multiply(BigDecimal("0.7"))

        userDB.moneyUpdate(event.author.info.money.add(money.toBigInteger()))
        logger.info("$[{event.author.idLong}] ${money} 원을 증가함 (현재 돈: ${event.author.info.money})")

        val embed = EmbedBuilder().setColor(DiscordColor.GREEN)
                .setTitle("반품완료")
                .setDescription("`${item.name}`을(를) 반품했어요")
                .addField("반품한 갯수", count.toString(), true)
                .addField("받은 돈", money.toBigInteger().toString(), true)
                .setFooter("반품하면 원가의 70%정도 돈을 돌려받습니다.")
                .build()

        event.channel.sendMessage(embed).queue()
    }
}