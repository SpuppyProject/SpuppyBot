package com.github.yeoj34760.spuppybot.commands

import com.github.yeoj34760.spuppybot.music.GuildManager
import com.github.yeoj34760.spuppybot.other.DiscordColor
import com.github.yeoj34760.spuppybot.other.MusicListBook
import com.github.yeoj34760.spuppybot.other.Util
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User
import java.lang.StringBuilder

object List : Command() {
    init {
        super.name = "list"
        super.aliases = arrayOf("list", "리스트", "ㅣㅑ", "li")
    }

    override fun execute(event: CommandEvent) {
        val id = event.guild.idLong

        if (GuildManager[id] == null || !GuildManager.get(id)!!.isPlayed()) {
            event.channel.sendMessage("뭔가 엄청난 걸 보여드리고 싶었지만 아쉽게도 아무 음악이 없네요").queue()
            return
        }

        //입력한 args가 없을 경우 1로 지정합니다.
        val pageNumber = if (event.args.isEmpty()) 1 else event.args.toInt()
        val playerControl = GuildManager.get(id)
        val book = MusicListBook(playerControl!!.trackQueue.toTypedArray())
        val playingTrack = GuildManager.get(event.guild.idLong)!!.playingTrack()
        val nextMusic = if (playerControl.isLooped) "무한 루프" else if(playerControl.playingTrack().info.isStream) "LIVE" else "${(playingTrack.duration - playingTrack.position) / 1000}초 남음"
        val list = if (playerControl.trackQueue.isEmpty()) "썰렁... 대기열에 아무 것도 없네요." else pageToString(book, pageNumber - 1)
        val pageContent = if (playerControl.trackQueue.isEmpty()) "page : 백지" else "page : ${pageNumber}/${book.count()}"


        val embed = EmbedBuilder()
                .setAuthor(event.author.name, null, event.author.avatarUrl)
                .setTitle("대기열 목록들 (${playerControl.trackQueue.size}개)")
                .setDescription(list)
                .addField("재생 중..", "[${playingTrack.info.title}](${playingTrack.info.uri})", true)
                .addField("다음 음악까지", nextMusic, true)
                .setFooter(pageContent)
                .setColor(DiscordColor.GREEN)
                .build()
        event.reply(embed)


    }

    private fun pageToString(book: MusicListBook, pageNumber: Int): String {
        var temp: StringBuilder = StringBuilder()
        var number: Int = pageNumber * book.MAX_PAGE
        for (trackNumber in 0 until book[pageNumber]!!.size) {
            var track = book.pageTrack(pageNumber, trackNumber)
            temp.append("${++number} > [${track!!.info.title}](${track.info.uri}) `${(track.userData as User).name}`\n\n")
        }
        return temp.toString()
    }
}