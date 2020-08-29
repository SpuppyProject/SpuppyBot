package com.github.yeoj34760.spuppybot.command

enum class CommandInfoName {
    BOX,
    ADD_BOX,
    LIST_BOX,
    REMOVE_BOX,
    REMOVE_ALL_BOX,
    COPY_ALL_BOX,
    MOVE_BOX,
    CONNECT,
    DISCONNECT,
    INFO,
    LIST,
    LOOP,
    NOWPLAY,
    PAUSE,
    PLAY,
    REMOVE,
    SEARCH,
    SKIP,
    SPEED,
    STOP,
    VOLUME;
    fun fromString() : String {
        return when(this) {
            BOX -> "box"
            ADD_BOX -> "addbox"
            LIST_BOX -> "listbox"
            REMOVE_BOX -> "removebox"
            REMOVE_ALL_BOX -> "removeallbox"
            COPY_ALL_BOX -> "copyallbox"
            MOVE_BOX -> "movebox"
            CONNECT -> "connect"
            DISCONNECT -> "disconnect"
            INFO -> "info"
            LIST -> "list"
            LOOP -> "loop"
            NOWPLAY -> "nowplay"
            PAUSE -> "pause"
            PLAY -> "play"
            REMOVE -> "remove"
            SEARCH -> "search"
            SKIP -> "skip"
            SPEED -> "speed"
            STOP -> "stop"
            VOLUME -> "volume"
        }
    }
}