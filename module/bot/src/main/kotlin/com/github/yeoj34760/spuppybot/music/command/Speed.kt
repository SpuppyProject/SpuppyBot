package com.github.yeoj34760.spuppybot.music.command


import com.github.yeoj34760.spuppybot.music.GuildManager.playerControls
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings

/**
 * 스피드를 조절해줍니다.
 */
@CommandSettings(name = "speed")
object Speed : Command() {

    override fun execute(event: CommandEvent) {
        //재생 중이지 않을 경우
        if (playerControls[event.guild.idLong] == null || !playerControls[event.guild.idLong]!!.isPlayed()) {
            event.channel.sendMessage("재생 중이지 않네요").queue()
            return
        }

        //재생 중인 음악이 라이브로 되어 있을 경우
        if (playerControls[event.guild.idLong]!!.playingTrack().info.isStream) {
            event.channel.sendMessage("라이브 음악은 speed 기능을 제공하지 않습니다.").queue()
            return
        }

        when (val number: Double? = event.args[0].toDoubleOrNull()) {
            //제대로 된 실수를 받지 못할 경우
            null -> event.channel.sendMessage("올바르지 않은 값이네요").queue()
            //올바르게 받을 경우
            in 0.1..5.0 -> {
                playerControls[event.guild.idLong]!!.speed(number)
                event.channel.sendMessage("`$number` 속도로 설정했습니다.").queue()
            }
            //미만이거나 초과할 경우
            else -> event.channel.sendMessage("값을 `0.1 ~ 5`로 지정해주세요").queue()
        }
    }

}