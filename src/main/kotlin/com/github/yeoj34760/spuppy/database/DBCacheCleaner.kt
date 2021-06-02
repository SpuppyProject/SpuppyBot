package com.github.yeoj34760.spuppy.database

import kotlinx.coroutines.*

class DBCacheCleaner(private val cache: DBCache) {
    private var job: Job? = null

    private fun thread() {
        job = GlobalScope.launch {
            while (true) {
                delay(1000 * 60 * 5)
                val currentTime = System.currentTimeMillis()
                for (user in cache.userList) {
                    if (currentTime - user.usedTime <= 1000 * 60 * 10)
                        cache.userList.remove(user)
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