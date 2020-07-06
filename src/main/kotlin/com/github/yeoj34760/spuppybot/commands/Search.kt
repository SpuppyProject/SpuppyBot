package com.github.yeoj34760.spuppybot.commands

import com.github.yeoj34760.spuppybot.other.Util
import com.github.yeoj34760.spuppybot.waiter
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.util.concurrent.TimeUnit

object Search : Command() {
    init {
        super.name = "search"
        super.aliases = arrayOf("search", "se")
    }

    override fun execute(event: CommandEvent) {



        event.reply("찾는중...") {
            var audioList = Util.youtubeSearch(event.args, it)
            var result: String = ""
            var i: Int = 1
            if (audioList == null) {
                return@reply
            } else if (audioList.tracks.isEmpty()) {
                it.editMessage("검색 결과가 없습니다").queue()
                return@reply
            }

            audioList.tracks.forEach {
                if (i - 1 == 5)
                    return@forEach
                result += "${i++}. [${it.info.title}](${it.info.uri})\n\n"
            }
            it.editMessage("아래에 있는 숫자중에 골라주세요").queue() {
                var embedId: Long? = null
                event.channel.sendMessage(createEmbed(event.args, result)).queue() {
                     embedId = it.idLong
                }
                waiter.waitForEvent(MessageReceivedEvent::class.java,
                        { e ->
                            e.author.equals(event.author)
                                    && e.channel.equals(event.channel)
                                    && !e.message.equals(event.message)
                        },

                        { e ->
                            //숫자아닐 경우 리턴합니다.
                            var number: Int? = e.message.contentRaw.toIntOrNull()
                            //1 ~ 5 사이에 맞지 않을 경우 넘어갑니다.
                            if (number != null && number in 1..5)
                                Util.youtubePlay(event, it, audioList.tracks[number!!].identifier)
                            else {
                                it.editMessage("취소됨").queue()
                                event.channel.deleteMessageById(embedId!!).queue()
                                return@waitForEvent
                            }

                        }, 1, TimeUnit.MINUTES) { event.reply("시간 초과됨") }
            }
        }
    }

    fun createEmbed(args: String, result: String): MessageEmbed = EmbedBuilder()
            .setTitle("SpuppyBot")
            .setDescription(result)
            .setFooter("입력받은 값 : $args")
            .build()
}