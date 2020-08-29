package com.github.yeoj34760.spuppybot.command

enum class CommandInfoGroup {
    BOX,
    MUSIC;
    fun fromString(): String {
        return when(this) {
            BOX -> "box"
            MUSIC -> "music"
        }
    }
}