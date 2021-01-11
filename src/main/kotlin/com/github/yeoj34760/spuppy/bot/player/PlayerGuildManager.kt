package com.github.yeoj34760.spuppy.bot.player

import com.github.yeoj34760.spuppy.bot.Bot
import net.dv8tion.jda.api.entities.Guild

object PlayerGuildManager {
    private val playerControls: MutableMap<Long, PlayerControl> = mutableMapOf()

    fun check(guildId: Long): Boolean =
        playerControls[guildId] != null && !playerControls[guildId]!!.isPlayed() || playerControls[guildId]!!.trackList()
            .isEmpty()

    fun create(guild: Guild): PlayerControl {
        if (playerControls.containsKey(guild.idLong))
            return playerControls[guild.idLong]!!

        val tempPlayer = Bot.playerManager.createPlayer()
        val tempPlayerControl = PlayerControl(tempPlayer)

        tempPlayer.addListener(tempPlayerControl)
        guild.audioManager.sendingHandler = PlayerSendHandler(tempPlayer)
        playerControls[guild.idLong] = tempPlayerControl

        return tempPlayerControl
    }

    fun remove(guild: Guild) = remove(guild.idLong)
    fun remove(guildId: Long) = playerControls.remove(guildId)
    fun count() = playerControls.count()

    operator fun get(guild: Guild): PlayerControl? = get(guild.idLong)
    operator fun get(guildId: Long): PlayerControl? = playerControls[guildId]
}