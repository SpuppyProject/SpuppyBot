package com.github.yeoj34760.spuppybot.sql.spuppydb

import com.github.yeoj34760.spuppybot.spuppyDBConnection

object GuildDBController {
    /**
     * 특정 길드 볼륨 값을 불러옵니다.
     */
    fun guildVolume(id: Long): Int {
        val t = spuppyDBConnection.createStatement().executeQuery("select volume from guild where id = $id")
        if (t.next())
            return t.getInt(1)
        return -1
    }

    /**
     * 특정 길드 볼륨 설정을 설정합니다.
     */
    fun guildVolume(id: Long, volume: Int) {
        val ps = spuppyDBConnection.prepareStatement("update guild set volume = ? where id = ?")
        ps.setInt(1, volume)
        ps.setLong(1, id)
        ps.executeQuery()

    }

    /**
     * 데이터베이스에 길드 아이디를 저장합니다.
     */
    fun addGuild(guildId: Long) {
        val ps = spuppyDBConnection.prepareStatement("insert into guild values (?, ?);")
        ps.setLong(1, guildId)
        ps.setInt(2, 100)
        ps.executeQuery()
    }

    /**
     * 데이터베이스에 있는 길드 정보를 삭제합니다.
     */
    fun delGuild(guildId: Long) {
        val ps = spuppyDBConnection.prepareStatement("delete from guild where id=?")
        ps.setLong(1, guildId)
        ps.executeQuery()
    }

    /**
     * 데이터베이스에 길드 정보가 있는지 확인합니다.
     * @return 있을 경우 true, 없을 경우 false
     * */
    fun checkGuild(guildId: Long): Boolean {
        val ps = spuppyDBConnection.prepareStatement("select exists(select * from guild where id=?)")
        ps.setLong(1, guildId)
        val result = ps.executeQuery()
        if (result.next())
            return result.getInt(1) == 1

        return false
    }
}

