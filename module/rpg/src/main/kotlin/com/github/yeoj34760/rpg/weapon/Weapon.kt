package com.github.yeoj34760.rpg.weapon

data class Weapon(val name: String,
                  val type: WeaponType,
                  val level: Int,
                  val power: Int,
val content: List<String>) {
}