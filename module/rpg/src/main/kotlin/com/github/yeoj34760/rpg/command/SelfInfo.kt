package com.github.yeoj34760.rpg.command

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.DiscordColor
import com.github.yeoj34760.spuppybot.db.rpg.Player
import net.dv8tion.jda.api.EmbedBuilder
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

@CommandSettings(name = "selfinfo", aliases = ["내정보"])
object SelfInfo : Command() {
    override fun execute(event: CommandEvent) {
        transaction {
            addLogger(StdOutSqlLogger)
          val player =   Player.select { Player.id eq event.author.idLong }.find { true }!!
            val embed = EmbedBuilder().setColor(DiscordColor.GREEN)
                    .setTitle("`${event.author.name}`님의 정보")
                    .addField("사용중인 무기", player[Player.weapon], true)
                    .addField("레벨", player[Player.level].toString(), true)
                    .addField("몬스터 킬 수", player[Player.monsterKill].toString(), true)
                    .build()
            event.channel.sendMessage(embed).complete()
        }
    }
}