package com.github.yeoj34760.spuppy.music

import com.github.yeoj34760.spuppy.bot.Bot
import com.github.yeoj34760.spuppy.database.DBController
import com.github.yeoj34760.spuppy.database.DBManager
import com.github.yeoj34760.spuppy.utilities.player.CallTrack
import com.github.yeoj34760.spuppy.utilities.player.PlayerUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.JDABuilder
import org.junit.Test

class MusicTest {
    private val urlForTest = "https://youtu.be/cPkE0IbDVs4"
    @Test
    fun getYoutube() {
        val jda = JDABuilder.createDefault(Bot.info.token).build()

        runBlocking {
            val callTrack = CallTrack.call(jda.retrieveUserById(Bot.info.ownerId).complete(), urlForTest)
            if (callTrack.isFailed)
                error("call failed!")
            println(callTrack)
        }
    }

    @Test
    fun testSearch() {
        val list = PlayerUtil.youtubeSearch("i love you") ?: error("search failed")
        list.tracks.forEach {
            println("search result: ${it.info.title}")
        }
    }

    @Test
    fun wow() {
        DBController.cache.addUser(10101010)
        val user = DBController.cache.addUser(10101010)
        println(user)
    }
}