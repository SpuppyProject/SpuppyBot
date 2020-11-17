package com.github.yeoj34760.spuppybot.rpg.weapon

data class Weapon(val name: String,
                  val type: WeaponType,
                  val level: Int,
                  val skills: List<Skill>) {
}


data class Skill(
        val name: String,
        val count: Int,
        val power: Int,
        val content: List<String>
)