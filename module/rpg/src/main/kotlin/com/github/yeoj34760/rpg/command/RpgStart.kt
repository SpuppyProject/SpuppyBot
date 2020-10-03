package com.github.yeoj34760.rpg.command

import com.github.yeoj34760.rpg.rpg
import com.github.yeoj34760.rpg.weapon.Weapon
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.DiscordColor
import com.github.yeoj34760.spuppybot.db.DB
import com.github.yeoj34760.spuppybot.db.rpg.Player
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.random.Random

@CommandSettings(name = "rpgstart", aliases = ["게임시작"])
object RpgStart : Command() {

    private data class GameInfo(var playerHP: Int = 100, var monsterHP: Int = Random.nextInt(150, 200), val monsterName: String, val weapon: Weapon)

    val playingChannels = mutableListOf<Long>()
    override fun execute(event: CommandEvent) {
        if (playingChannels.contains(event.channel.idLong)) {
            event.channel.sendMessage("현재 이곳에 누군가 게임 중이예요! 기다려보시거나 다른 곳에서 해보세요!").complete();return
        }
        val dungeon = rpg.dungeonList.find { it.name == event.content }
        if (event.content.isEmpty()) {
            event.channel.sendMessage("던전 이름을 써주세요!").complete();return
        }
        if (dungeon == null) {
            event.channel.sendMessage("해당 이름을 가진 던전을 찾을 수 없어요!").complete();return
        }

        gameStart(event)
    }

    /**
     * 조건이 맞으면 게임 시작합니다.
     */
    private fun gameStart(event: CommandEvent) {
        GlobalScope.launch {
            playingChannels.add(event.channel.idLong)
            val gameInfo = createGameInfo(event)
            val findMessage = event.channel.sendMessage("`${event.content}`에서 찾는 중...").complete()
            delay(1000)
            findMessage.editMessage("`${gameInfo.monsterName}`을(를) 찾았다!\n그리고 당신은 몬스터를 때렸다!").complete()
            var gameMessage = event.channel.sendMessage(monsterDamage(event, gameInfo)).complete()

            while (gameInfo.monsterHP > 0 && gameInfo.playerHP > 0) {
                delay(1000)
                when (Random.nextBoolean()) {
                    true -> gameMessage.editMessage(playerDamage(event, gameInfo)).complete()
                    false -> gameMessage.editMessage(monsterDamage(event, gameInfo)).complete()
                }
            }

            gameMessage.delete().complete()
            when {
                gameInfo.playerHP >= 0 -> event.channel.sendMessage("졌다 씨발!").complete()
                gameInfo.monsterHP >= 0 -> event.channel.sendMessage("이겼다! 씨발!").complete()
            }
            playingChannels.remove(event.channel.idLong)
        }
    }

    private fun playerDamage(event: CommandEvent, gameInfo: GameInfo): MessageEmbed {
        val damage = Random.nextInt(3, 8)
        gameInfo.playerHP -= damage

        val text = "당신은 ${gameInfo.monsterName}한테 맞아서 피가 깍였다! 아프겠다!"
        return basicEmbed(event, gameInfo, DiscordColor.RED, text)
    }

    private fun monsterDamage(event: CommandEvent, gameInfo: GameInfo): MessageEmbed {
        val damage = Random.nextInt(6, 12)
        gameInfo.monsterHP -= damage

        val text = "${gameInfo.weapon.content.random().replace("{monster_name}", gameInfo.monsterName)}\n ${gameInfo.monsterName} 체력이 ${damage}이나 깍였다!"
        return basicEmbed(event, gameInfo, DiscordColor.GREEN, text)
    }

    private fun basicEmbed(event: CommandEvent, gameInfo: GameInfo, color: Int, text: String): MessageEmbed {
        return EmbedBuilder().setTitle("${event.content}에서 전투 중!")
                .setColor(color)
                .setDescription(text)
                .addField("player HP", gameInfo.playerHP.toString(), true)
                .addField("${gameInfo.monsterName} HP", gameInfo.monsterHP.toString(), true)
                .build()
    }

    private fun createGameInfo(event: CommandEvent): GameInfo {
        return transaction(DB.rpgDB) {
            val weaponName = Player.select { Player.id eq event.author.idLong }.first()[Player.weapon]
            val weapon = rpg.weaponList.first { it.name == weaponName }
            val monsterName = rpg.dungeonList.first { it.name == event.content }.monsters.random()
            GameInfo(monsterName = monsterName, weapon = weapon)
        }
    }
}