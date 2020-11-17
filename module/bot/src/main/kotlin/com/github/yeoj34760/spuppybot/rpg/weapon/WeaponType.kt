package com.github.yeoj34760.spuppybot.rpg.weapon

enum class WeaponType(val alias: String, val content: String) {
    RANGED("원거리","몬스터 눈을 찔려 죽입니다."),
    MELEE("근접","무식하게 죽입니다."),
    LANGUAGE("언어","몬스터한테 언어폭행해서 자살하게 만듭니다."),
    GUN("총","총으로 몬스터 심장을 관통합니다.")
}