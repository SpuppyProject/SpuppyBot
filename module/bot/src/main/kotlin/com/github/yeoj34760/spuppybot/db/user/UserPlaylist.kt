package com.github.yeoj34760.spuppybot.db.user

import com.github.yeoj34760.spuppybot.TrackManager
import com.sedmelluq.discord.lavaplayer.track.AudioTrack


typealias UserPlaylist = HashMap<String, MutableList<String>>


object UserPlaylistManager {
    fun trackDecode(userPlaylist: UserPlaylist, name: String, num: Int): AudioTrack {
        val track = userPlaylist[name]?.get(num) ?: throw Exception("찾을 수 없음")
        return TrackManager.base64ToTrack(track)
    }

    fun tracksDecode(userPlaylist: UserPlaylist, name: String): List<AudioTrack> {
        val encodeTracks = userPlaylist[name] ?: throw Exception("찾을 수 없음")
        val decodeTracks = mutableListOf<AudioTrack>()
        encodeTracks.forEach { decodeTracks.add(TrackManager.base64ToTrack(it)) }
        return decodeTracks
    }

    fun addTrack(userPlaylist: UserPlaylist, name: String, track: AudioTrack) {
        val encodeTrack = TrackManager.trackToBase64(track)
        userPlaylist[name]?.add(encodeTrack)
    }
}

//@Serializable
//class UserPlaylist : HashMap<String, MutableList<String>>() {
//    fun trackDecode(name: String, num: Int) : AudioTrack {
//        val track = this[name]?.get(num) ?: throw Exception("찾을 수 없음")
//        return TrackManager.base64ToTrack(track)
//    }
//
//    fun tracksDecode(name: String) : List<AudioTrack> {
//        val encodeTracks = this[name] ?: throw Exception("찾을 수 없음")
//        val decodeTracks = mutableListOf<AudioTrack>()
//        encodeTracks.forEach { decodeTracks.add(TrackManager.base64ToTrack(it)) }
//        return decodeTracks
//    }
//
//    fun addTrack(name: String, track: AudioTrack) {
//        val encodeTrack = TrackManager.trackToBase64(track)
//        this[name]?.add(encodeTrack)
//    }
//}
