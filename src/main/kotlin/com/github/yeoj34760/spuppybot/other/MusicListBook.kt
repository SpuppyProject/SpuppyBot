package com.github.yeoj34760.spuppybot.other

import com.sedmelluq.discord.lavaplayer.track.AudioTrack


class MusicListBook(tracks: Array<AudioTrack>) {
    val MAX_PAGE = 5
    private var pages: ArrayList<ArrayList<AudioTrack>> = ArrayList()

    init {
        var maxPage = tracks.size / MAX_PAGE
        var remainder = tracks.size % MAX_PAGE
        for (i in 0 until maxPage) {
            var page = ArrayList<AudioTrack>()
            for (j in MAX_PAGE * i until MAX_PAGE * (i + 1))
                page.add(tracks[j])
            pages.add(page)
        }
        var page = ArrayList<AudioTrack>()

        if (remainder != 0) {
            for (i in maxPage * MAX_PAGE until (maxPage * MAX_PAGE) + remainder)
                page.add(tracks[i])
            pages.add(page)
        }
    }


    operator fun get(number: Int): ArrayList<AudioTrack>? = pages.getOrNull(number)

    fun pageTrack(pageNumber: Int, trackNumber: Int): AudioTrack? = pages[pageNumber][trackNumber]

    fun count() = pages.size
}