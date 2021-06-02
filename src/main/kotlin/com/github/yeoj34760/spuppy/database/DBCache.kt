package com.github.yeoj34760.spuppy.database

import com.github.yeoj34760.spuppy.database.manager.BlackUser
import java.math.BigDecimal

class DBCache {
    internal val userList: MutableList<User> = mutableListOf()
    private val updateUserList: MutableList<User> = mutableListOf()
    private val blackList: MutableList<BlackUser> = mutableListOf()

    fun updateUserEvent(user: User) = updateUserList.add(user)
    fun getUser(id: Long): User? = userList.firstOrNull { it.id == id } ?: DBController.manager.loadUser(id)
    fun addUser(id: Long): User {
        val user = User(id, BigDecimal("0"))
        DBController.manager.addUser(user)
        return user
    }
}