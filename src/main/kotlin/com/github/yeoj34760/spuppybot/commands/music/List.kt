package com.github.yeoj34760.spuppybot.commands.music


import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.music.GuildManager.playerControls
import com.github.yeoj34760.spuppybot.music.PlayerControl
import com.github.yeoj34760.spuppybot.other.DiscordColor
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.User
import kotlin.math.ceil


@CommandSettings(name = "list")
object List : Command() {

    override fun execute(event: CommandEvent) {
        val id = event.guild.idLong

        if (playerControls[id] == null || !playerControls[id]!!.isPlayed()) {
            event.channel.sendMessage("뭔가 엄청난 걸 보여드리고 싶었지만 아쉽게도 아무 음악이 없네요").queue()
            return
        }
        val playerControl = playerControls[id]

        //입력한 args가 없을 경우 1로 지정합니다.
        val number = if (event.args.isEmpty()) 1 else event.args[0].toIntOrNull() ?: 1

        if (number > ceil(playerControl!!.trackQueue.size / 5f))
        {
            event.channel.sendMessage("숫자를 ${ceil(playerControl.trackQueue.size / 5f).toInt()} 이하로 지정해주세요!").queue()
            return
        }

        else if (number <= 0) {
            event.channel.sendMessage("1이상 지정해주세요!").queue()
            return
        }

        val playingTrack = playerControl.playingTrack()
        playerControl.trackQueue.toList()
        val nextMusic = if (playerControl.isLooped) "무한 루프" else if (playerControl.playingTrack().info.isStream) "LIVE" else "${(playingTrack.duration - playingTrack.position) / 1000}초 남음"
        val list = if (playerControl.trackQueue.isEmpty()) "썰렁... 대기열에 아무 것도 없네요." else listToString(playerControl, number)
        val pageContent = if (playerControl.trackQueue.isEmpty()) "page : 백지" else "page : ${number}/${ceil(playerControl.trackQueue.size / 5f).toInt()}"


        val embed = EmbedBuilder()
                .setAuthor(event.author.name, null, event.author.avatarUrl)
                .setTitle("대기열 목록들 (${playerControl.trackQueue.size}개)")
                .setDescription(list)
                .addField("재생 중..", "[${playingTrack.info.title}](${playingTrack.info.uri})", true)
                .addField("다음 음악까지", nextMusic, true)
                .setFooter(pageContent)
                .setColor(DiscordColor.GREEN)
                .build()
        event.channel.sendMessage(embed).queue()


    }

    private fun listToString(playerControl: PlayerControl, num: Int): String {
        val temp: StringBuffer = StringBuffer()
        val list = playerControl.trackQueue.toList()
        for (i in (num-1)*5 until num*5) {
            if (list.size <=i)
                break

            temp.append("**${i+1}.** [${list[i].info.title ?: "제목 알 수 없음!"}](${list[i].info.uri}) 신청자: `${(list[i].userData as User).name}`\n\n")
        }

        return temp.toString()
    }
}