package com.github.yeoj34760.spuppybot

data class CmdJson(val commands: List<Cmd>)
data class Cmd(val name: String, val command: List<String>)