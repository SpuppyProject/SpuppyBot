package com.github.yeoj34760.rpg.command

import com.github.yeoj34760.rpg.Dungeon
import com.github.yeoj34760.rpg.Player
import com.github.yeoj34760.rpg.rpg
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.DiscordColor
import com.github.yeoj34760.spuppybot.db.DB
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.random.Random

@CommandSettings(name = "rpgstart", aliases = ["게임시작"])
object RpgStart : Command() {


    private class Waiter(val event: CommandEvent) : ListenerAdapter() {
        private var isAnswer = false
        private var answer: String = String()
        var isTimeout: Boolean = false
            private set

        suspend fun playerSelectNumber(): Int? {
            var timeout = 0
            while (timeout < 600) {
                delay(100)
                timeout++
                if (isAnswer)
                    return answer.toIntOrNull()
            }
            isTimeout = true
            return null
        }

        override fun onMessageReceived(waiterEvent: MessageReceivedEvent) {
            isAnswer = (event.channel.id == waiterEvent.channel.id && event.author.id == event.author.id)
            answer = waiterEvent.message.contentRaw
        }
    }

    private data class GameInfo(var playerHP: Int = 100,
                                val player: Player,
                                var monsterHP: Int,
                                val monsterName: String,
                                val monsterLevel: Int)

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

        gameStart(event, dungeon)
    }

    /**
     * 조건이 맞으면 게임 시작합니다.
     */
    private fun gameStart(event: CommandEvent, dungeon: Dungeon) {
        GlobalScope.launch {
            playingChannels.add(event.channel.idLong)
            val gameInfo = createGameInfo(event, dungeon)
            val findMessage = event.channel.sendMessage("`${event.content}`에서 찾는 중...").complete()
            delay(1000)
            findMessage.editMessage("`${gameInfo.monsterName}`을(를) 찾았다!\n그리고 당신은 몬스터를 때렸다!").complete()
            event.channel.sendMessage(monsterDamage(event, gameInfo)).complete()

            var playerTurn = false
            while (gameInfo.monsterHP > 0 && gameInfo.playerHP > 0) {
                delay(1000)
                when (playerTurn) {
                    false -> event.channel.sendMessage(playerDamage(event, gameInfo)).complete()
                    true -> event.channel.sendMessage(monsterDamage(event, gameInfo)).complete()
                }

                playerTurn = playerTurn.not()
            }

            when {
                gameInfo.playerHP <= 0 -> event.channel.sendMessage("졌다 씨발!").complete()
                gameInfo.monsterHP <= 0 -> gameWin(event, gameInfo, dungeon)
            }
            playingChannels.remove(event.channel.idLong)
        }
    }

    private fun gameWin(event: CommandEvent, gameInfo: GameInfo, dungeon: Dungeon) {
        event.channel.sendMessage("이겼다! 씨발!").complete()
        val temp = dungeon.dropWeaponList.random()
        println(temp)
        val weapon = rpg.weaponList.first { it.name == temp }
        gameInfo.player.addWeapon(weapon)
        gameInfo.player.addMonsterKill()
        event.channel.sendMessage("${weapon.name}을(를) 얻었다! 씨발!").complete()
    }

    private fun playerDamage(event: CommandEvent, gameInfo: GameInfo): MessageEmbed {
        val damage = Random.nextInt(5, 8)
        gameInfo.playerHP -= damage

        val text = "당신은 ${gameInfo.monsterName}한테 맞아서 피가 깍였다! 아프겠다!"
        return basicEmbed(event, gameInfo, DiscordColor.RED, text)
    }

    private suspend fun monsterDamage(event: CommandEvent, gameInfo: GameInfo): MessageEmbed {
        val embedBuilder = EmbedBuilder().setColor(DiscordColor.BLUE)
                .setTitle("당신 차례가 왔다!")
        var tempNum = 1
        gameInfo.player.weapon.skills.forEach { embedBuilder.addField("${tempNum++}번", "**${it.name}**\n횟수: ${it.count}", true) }
        val embed = embedBuilder.build()
        event.channel.sendMessage(embed).complete()
        val waiter = Waiter(event)
        event.jda.addEventListener(waiter)
        val selectNum: Int? = waiter.playerSelectNumber()
        if (waiter.isTimeout) {
            event.channel.sendMessage("아니 님 뭐함").complete()
        }
        event.jda.removeEventListener(waiter)

        val skill = gameInfo.player.weapon.skills[selectNum!! - 1]

        val damage = skill.power + Random.nextInt(6, 12)
        gameInfo.monsterHP -= damage

        val text = "${skill.content.random().replace("{monster_name}", gameInfo.monsterName)}\n ${gameInfo.monsterName} 체력이 ${damage}이나 깍였다!"
        return basicEmbed(event, gameInfo, DiscordColor.GREEN, text)
    }


    private fun basicEmbed(event: CommandEvent, gameInfo: GameInfo, color: Int, text: String): MessageEmbed {
        val tempMap = HashMap<String, Int>()
        return EmbedBuilder().setTitle("${event.content}에서 전투 중!")
                .setColor(color)
                .setDescription(text)
                .addField("player HP", gameInfo.playerHP.toString(), true)
                .addField("${gameInfo.monsterName} HP", gameInfo.monsterHP.toString(), true)
                .build()
    }

    private fun createGameInfo(event: CommandEvent, dungeon: Dungeon): GameInfo {
        return transaction(DB.rpgDB) {
            val player = Player.create(event.author.idLong)
            val monsterName = dungeon.monsters.random()
            var monsterLevel = dungeon.level
            if (player.level != 1)
                monsterLevel += Random.nextInt(-1, 1)

            val monsterHP = Random.nextInt(100 + (monsterLevel * 50), 150 + (monsterLevel * 50))
            GameInfo(monsterName = monsterName, player = player, monsterLevel = monsterLevel, monsterHP = monsterHP)
        }
    }
}