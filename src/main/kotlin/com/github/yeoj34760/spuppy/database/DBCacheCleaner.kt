package com.github.yeoj34760.spuppy.database

import kotlinx.coroutines.*

class DBCacheCleaner(private val cache: DBCache) {
    private var job: Job? = null

    private fun thread() {
        job = GlobalScope.launch {
            while (true) {
                delay(1000 * 60 * 1)
                val currentTime = System.currentTimeMillis()
                val removeList: MutableList<User> = mutableListOf()
                for (user in cache.userList) {
                    if (currentTime - user.usedTime <= 1000 * 60 * 1)
                        removeList.add(user)
                }

                for (user in removeList)
                    cache.userList.remove(user)

                val tempUpdateUserList = cache.updateUserList.toMutableList()
                cache.updateUserList.clear()
                for (user in tempUpdateUserList) {
                    DBController.manager.updateUser(user)
                }
            }
        }
    }

    fun start() {
        thread()
    }

    fun stop() {
        if (job != null)
            job!!.cancel()
    }
}