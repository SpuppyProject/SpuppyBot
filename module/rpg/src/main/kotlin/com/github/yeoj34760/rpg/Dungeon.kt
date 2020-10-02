package com.github.yeoj34760.rpg

import com.google.gson.annotations.SerializedName

data class Dungeon(
        val name: String,
        val monsters: List<String>,
                   @SerializedName("drop_weapons")
                   val dropWeaponList: List<String>) {
}