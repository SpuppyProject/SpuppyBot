package com.github.yeoj34760.spuppy.bot.player

import com.github.yeoj34760.spuppy.bot.Bot
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.dv8tion.jda.api.entities.User

object CallTrack {
    data class CallLTrackResult(
        val playlist: AudioPlaylist?,
        val track: AudioTrack?,
        val isFailed: Boolean,
        val exceptionContent: String?,
        val isNoMatches: Boolean
    )

    suspend fun call(user: User, url: String): CallLTrackResult {
        var tempPlaylist: AudioPlaylist? = null
        var tempTrack: AudioTrack? = null
        var isFailed: Boolean = false
        var exceptionContent: String? = null
        var isNoMatches = false
        val mutex = Mutex()


        //결과가 나올 때까지 대기합니다.
        mutex.withLock {
            Bot.playerManager.loadItem(url, object : AudioLoadResultHandler {
                override fun trackLoaded(track: AudioTrack) {
                    tempTrack = track
                    mutex.unlock()

                }

                override fun playlistLoaded(playlist: AudioPlaylist) {
                    tempPlaylist = playlist
                    tempTrack = playlist.tracks.first()
                    mutex.unlock()
                }

                override fun noMatches() {
                    isFailed = true
                    isNoMatches = true
                    mutex.unlock()
                }

                override fun loadFailed(exception: FriendlyException) {
                    isFailed = true
                    exceptionContent = exception.message
                    mutex.unlock()
                }
            })
            mutex.lock()
        }

        tempTrack?.userData = user
        tempPlaylist?.tracks?.forEach { it.userData = user }
        return CallLTrackResult(tempPlaylist, tempTrack, isFailed, exceptionContent, isNoMatches)
    }
}