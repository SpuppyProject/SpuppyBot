package com.github.yeoj34760.rpg

import com.github.yeoj34760.rpg.weapon.Weapon
import com.github.yeoj34760.spuppybot.db.DateTimeSerializer
import kotlinx.serialization.Serializable
import org.joda.time.DateTime



@Serializable
class PlayerWeapon(val name: String,
                   @Serializable(with = DateTimeSerializer::class)
                   var time: DateTime, var count: Int)