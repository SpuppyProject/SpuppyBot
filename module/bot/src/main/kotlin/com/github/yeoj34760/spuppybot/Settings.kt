package com.github.yeoj34760.spuppybot

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.jagrosh.jdautilities.commons.waiter.EventWaiter
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import java.io.File

val settings: Settings = Gson().fromJson(File("settings.json").readText(), Settings::class.java)
val waiter = EventWaiter()
val playerManager = DefaultAudioPlayerManager()
val cmdJson: CmdJson = Gson().fromJson(File("commands.json").readText(), CmdJson::class.java)


data class Settings(val token: String,
                    val prefix: String,
                    @SerializedName("owner_id")
                    val ownerId: String,
                    val version: String,
                    val db: DataDB)

data class DataDB(
        @SerializedName("rpg_url")
        val rpgUrl: String,val url: String, val user: String, val password: String)
