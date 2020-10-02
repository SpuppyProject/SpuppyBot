package com.github.yeoj34760.rpg.command

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.db.rpg.Player
import com.github.yeoj34760.spuppybot.settings
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

@CommandSettings(name="selfinfo", aliases = ["내정보"])
object SelfInfo : Command() {
    override fun execute(event: CommandEvent) {
        transaction {
            addLogger(StdOutSqlLogger)
            for (player in Player.selectAll()) {
                event.channel.sendMessage("${player[Player.id]}: ${player[Player.weapon]}").complete()
            }
        }
    }
}