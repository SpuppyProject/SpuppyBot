package com.github.yeoj34760.spuppybot.sql.userbox

import kotlinx.serialization.Serializable

@Serializable
data class UserBoxInfo(
        //영상 타이틀
        val title: String,
        //영상 길이
        val length: Long,
        //영상 URL
        val url: String,
        //신청한 사람
        val author: String,
        //영상 만든이
        val creator: String
)