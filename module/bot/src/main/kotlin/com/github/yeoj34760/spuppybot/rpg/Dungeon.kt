package com.github.yeoj34760.spuppybot.rpg

import com.google.gson.annotations.SerializedName

data class Dungeon(
        val name: String,
        val monsters: List<String>,
        val level: Int,
        @SerializedName("drop_weapons")
        val dropWeaponList: List<String>) {
}