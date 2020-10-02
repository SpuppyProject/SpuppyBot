package com.github.yeoj34760.rpg

import com.github.yeoj34760.rpg.weapon.Weapon
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.File

val rpg = Gson().fromJson(File("rpg.json").readText(), RPG::class.java)

data class RPG(
        @SerializedName("dungeons")
        val dungeonList: List<Dungeon>,
        @SerializedName("weapons")
        val weaponList: List<Weapon>) {
}