package com.github.yeoj34760.spuppybot.other

import java.text.SimpleDateFormat

object Util {
    /**
     * 식별자 이용해 썸네일 링크를 반환합니다.
     */
    fun youtubeToThumbnail(Identifier: String): String = "https://img.youtube.com/vi/$Identifier/mqdefault.jpg"


    fun timeMaker(milliseconds: Int) = SimpleDateFormat("hh시 mm분 ss초").format(milliseconds)
}