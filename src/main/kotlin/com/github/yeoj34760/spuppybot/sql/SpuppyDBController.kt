package com.github.yeoj34760.spuppybot.sql

import com.github.yeoj34760.spuppybot.Settings
import java.sql.DriverManager

object SpuppyDBController {
    val connection =
            DriverManager.getConnection(
                    Settings.SPUPPYDB_URL,
                    Settings.SPUPPYDB_USER,
                    Settings.SPUPPYDB_PASSWORD)


    /**
     * 특정 길드 볼륨 값을 불러옵니다.
     */
    fun guildVolume(id: Long): Int {
        val t = connection.createStatement().executeQuery("select volume from guild where id = $id")
        if (t.next())
            return t.getInt(1)
        return -1
    }

    /**
     * 특정 길드 볼륨 설정을 설정합니다.
     */
    fun guildVolume(id: Long, volume: Int) = connection.createStatement().execute("update guild set volume = $volume where id = $id")

    /**
     * 데이터베이스에 특정 길드를 추가합니다.
     */
    fun addGuild(id: Long) = add(id, "guild")

    /**
     * 데이터베이스에 있는 길드 정보를 삭제합니다.
     */
    fun delGuild(id: Long) = del(id, "guild")

    /**
     * 데이터베이스에 길드 정보가 있는지 확인합니다.
     * @return 있을 경우 true, 없을 경우 false
     * */
    fun checkGuild(id: Long): Boolean = check(id, "guild")

    fun addUserBox(id: Long, name: String, url: String) = connection.createStatement().execute("insert into user_box (id, url, name, number) values ($id, '$url', '$name', ${fromMaxNumber(id) + 1})")
    fun delAllUserBox(id: Long) = del(id, "user_box")
    fun delUserBox(id: Long, number: Int) = connection.createStatement().execute("delete from user_box where id = $id and number = $number")
    fun checkUserBox(id: Long): Boolean = check(id, "user_box")
    fun fromUserBox(id: Long): List<UserBox> {
        val tempBox = arrayListOf<UserBox>()
        val t = connection.createStatement().executeQuery("select url, name, number from user_box where id = $id")
        while (t.next())
            tempBox.add(UserBox(t.getString(1), t.getString(2), t.getInt(3)))

        return tempBox
    }

    /**
     * 해당 유저박스에서 몇 개 있는지 반환합니다.
     * 찾을 수 없을 경우 0로 반환합니다.
     */
    fun fromMaxNumber(id: Long): Int {
        val t = connection.createStatement().executeQuery("select max(number) from user_box where id = $id")
        if (t.next()) return t.getInt(1)
        return 0
    }

    fun addUser(id: Long) = add(id, "user")
    fun delUser(id: Long) = del(id, "user")
    fun checkUser(id: Long): Boolean = check(id, "user")


    /**
     * 입력한 커맨드가 데이터베이스에 등록되어 있는지 확인합니다.
     */
    fun checkCommand(name: String, command: String): Boolean {
        val t = connection.createStatement().executeQuery("select command from command where name = '$name'")
        while (t.next()) {
            if (command.startsWith(Settings.PREFIX + t.getString(1)))
                return true
        }

        return false
    }

    fun checkCommandGroup(group: String, command: String): Boolean {
        val t = connection.createStatement().executeQuery("select command from command where _group = '$group'")
        while (t.next()) {
            if (command.startsWith(Settings.PREFIX + t.getString(1)))
                return true
        }

        return false
    }

    fun commandFromList(name: String): List<String> {
        val t = connection.createStatement().executeQuery("select command from command where name = '$name'")
        val temp = arrayListOf<String>()
        while (t.next())
            temp.add(t.getString(1))
        return temp
    }

    private fun add(id: Long, table: String) = connection.createStatement().execute("insert into $table (id) values ($id)")
    private fun del(id: Long, table: String) = connection.createStatement().execute("delete from $table where id = $id")
    private fun check(id: Long, table: String): Boolean {
        val t = connection.createStatement().executeQuery("select count(*) from $table where id = $id")
        return t.next() && t.getInt(1) >= 1
    }
}