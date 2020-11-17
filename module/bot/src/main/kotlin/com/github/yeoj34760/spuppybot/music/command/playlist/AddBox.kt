package com.github.yeoj34760.spuppybot.music.command.playlist

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.db.UserDB
import com.github.yeoj34760.spuppybot.db.user.info
import com.github.yeoj34760.spuppybot.playerManager
import com.github.yeoj34760.spuppybot.settings
import com.github.yeoj34760.spuppybot.music.Util
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.entities.Message

/**
 * ?playlist add (my playlist name) (A music name or URI)
 */
@CommandSettings(name = "addplaylist")
object AddPlaylist : Command() {
    override fun execute(event: CommandEvent) {
        if (event.args.isEmpty()) {
            event.channel.sendMessage("명령어를 제대로 적어주세요.\n예시: `${settings.prefix}playlist add https://youtu.be/HZo539SVHJ4`").queue()
            return
        }

        if (event.author.info().playlist.isEmpty()) {
            event.channel.sendMessage("생성된 플레이리스트가 없어요!").complete()
            return
        }

        val (content, playlistName) = playlistNameFilter(event) ?: (event.content to event.author.info().playlist.keys.first())

        event.channel.sendMessage("검색 중...").queue {
            if (Util.checkURL(content)) {
                searchToUserPlaylist(event, it, playlistName)
            } else {
                val search = Util.youtubeSearch(content, it) ?: return@queue
                if (search.tracks!!.isEmpty()) {
                    event.channel.sendMessage("이런 검색결과가 없네요").queue()
                }
                it.editMessage("`${search.tracks[0].info.title}`를(을) 추가 되었어요!").queue()
                sendplaylist(event, search.tracks[0], playlistName)
            }
        }
    }

    /**
     * @return A - 필터링된 메세지, B - 찾은 플레이리스트 이름
     */
    private fun playlistNameFilter(event: CommandEvent) : Pair<String, String>? {
    val content = event.content
    val playlistNameList = event.author.info().playlist.keys.sortedBy { it.toInt() }
    for (name in playlistNameList) {
        if (content.startsWith(name))
            return Pair(content.substring(name.length).trim(), name)
    }

    return null
}

    private fun searchToUserPlaylist(event: CommandEvent, message: Message, playlistName: String) {
        playerManager.loadItem(event.content, object : AudioLoadResultHandler {
            override fun loadFailed(exception: FriendlyException) {
                message.editMessage("umm... 불행하게도 로드 실패했어요.").queue()
            }

            override fun trackLoaded(track: AudioTrack) {
                sendplaylist(event, track, playlistName)
                message.editMessage("`${track.info.title}`를(을) 추가했어요!").queue()
            }

            override fun noMatches() {
                message.editMessage("umm... 유감스럽게도 매치가 읎어요").queue()
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                playlist.tracks.forEach { track ->
                    sendplaylist(event, track, playlistName)
                }
            }
        })
    }

    private fun sendplaylist(event: CommandEvent, track: AudioTrack , playlistName: String) {
        UserDB(event.author.idLong).playlistAdd(playlistName, track)
    }
}