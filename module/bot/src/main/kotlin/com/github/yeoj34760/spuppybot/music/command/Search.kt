package com.github.yeoj34760.spuppybot.music.command


import com.github.yeoj34760.spuppybot.music.Util
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.waiter
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.util.concurrent.TimeUnit


@CommandSettings(name = "search")
object Search : Command() {

    override fun execute(event: CommandEvent) {
        if (!event.member?.voiceState!!.inVoiceChannel()) {
            event.channel.sendMessage("음성 방에 들어주세요! \n").queue()
        }

        if (event.args.isEmpty()) {
            event.channel.sendMessage("올바르게 써주세요! \n`예시: ?search 진진자라`").queue()
            return
        }

        val channel = event.member!!.voiceState!!.channel!!
        if (!event.guild.selfMember.hasPermission(channel, Permission.VOICE_CONNECT)) {
            event.channel.sendMessage("${event.member.nickname ?: event.author.name}님이 들어와 있는 채널에 권한이 없어 들어갈 수가 없어요!").queue()
            return
        }

        event.channel.sendMessage("놀라운 기술력으로 찾는중...").queue {
            val audioList = Util.youtubeSearch(event.args[0], it)
            var result = ""
            var i = 1

            //audioList 변수가 null일 경우 예외가 발생한 경우이니 리턴함
            if (audioList == null) {
                return@queue
            }
            //audioList 변수에 아무 것도 없다면 검색결과가 없다고 메세지를 보내고 리턴함.
            else if (audioList.tracks.isEmpty()) {
                it.editMessage("검색 결과가 없습니다").queue()
                return@queue
            }

            //audioList에 있는 트랙들의 데이터를 이용해 result에 추가함
            audioList.tracks.forEach { audioTrack ->
                if (i - 1 == 5)
                    return@forEach
                result += "${i++}. [${audioTrack.info.title}](${audioTrack.info.uri})\n\n"
            }

            //숫자고르라고 메세지로 수정함.
            it.editMessage("아래에 있는 숫자중에 골라주세요").queue {
                var embedId: Long? = null
                event.channel.sendMessage(createEmbed(event.args[0], result)).queue { message ->
                    embedId = message.idLong
                }
                waiter.waitForEvent(MessageReceivedEvent::class.java,
                        { e ->
                            e.author == event.author
                                    && e.channel == event.channel
                                    && e.message != event.message
                        },

                        { e ->
                            //숫자아닐 경우 리턴합니다.
                            val number: Int? = e.message.contentRaw.toIntOrNull()
                            //1 ~ 5 사이에 맞지 않을 경우 넘어갑니다.
                            if (number != null && number in 1..5) {
                                event.channel.deleteMessageById(embedId!!).queue()
                                Util.youtubePlay(event, it, audioList.tracks[number - 1].identifier)
                            } else {
                                it.editMessage("취소됨").queue()
                                event.channel.deleteMessageById(embedId!!).queue()
                                return@waitForEvent
                            }

                        }, 1, TimeUnit.MINUTES) { }
            }
        }
    }

    private fun createEmbed(args: String, result: String): MessageEmbed = EmbedBuilder()
            .setTitle("SpuppyBot")
            .setDescription(result)
            .setFooter("입력받은 값 : $args")
            .build()
}