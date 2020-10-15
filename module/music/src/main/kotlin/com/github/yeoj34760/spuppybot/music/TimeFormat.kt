package com.github.yeoj34760.spuppybot.music

object TimeFormat {
   private data class Time(private val time: Long) {
        val secondTime = time / 1000
        val day = secondTime / 86400
        val hour = (secondTime % (86400)) / 3600
        val minute = (secondTime % (3600)) / 60
        val second = (secondTime % (60))
    }
    fun simple(time: Long): String {
        val t = Time(time)

        return when {
            86400 <= t.secondTime -> "${t.day}:${t.hour}:${t.minute}:${t.second}"
            3600 <= t.secondTime -> "${t.hour}:${t.minute}:${t.second}"
            60 <= t.secondTime -> "${t.minute}:${t.second}"
            else -> "${t.second}"
        }
    }

    fun hangul(time: Long): String {
        val t = Time(time)

        return when {
            86400 <= t.secondTime -> "${t.day}일 ${t.hour}시 ${t.minute}분 ${t.second}초"
            3600 <= t.secondTime -> "${t.hour}시 ${t.minute}분 ${t.second}초"
            60 <= t.secondTime -> "${t.minute}분 ${t.second}초"
            else -> "${t.second}초"
        }
    }
}