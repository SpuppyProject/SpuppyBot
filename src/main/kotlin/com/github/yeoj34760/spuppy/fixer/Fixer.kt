package com.github.yeoj34760.spuppy.fixer


import com.google.gson.Gson
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.math.BigDecimal
import java.math.RoundingMode

data class Fixer(
    val timestamp: Long,
    val base: String,
//    val date: ,
    val rates: Map<String, BigDecimal>
) {
    companion object {
        fun create(api: String): Fixer? {
            val url = "http://data.fixer.io/api/latest?access_key=${api}&symbols=GBP,JPY,USD,KRW"
            val jsonString = getJsonData(url) ?: return null
            println(jsonString)
            return try {
                Gson().fromJson(jsonString, Fixer::class.java)
            } catch (e: Exception) {
                null
            }
        }

        private fun getJsonData(url: String): String? {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()

            val response = try {
                client.newCall(request).execute()
            } catch (e: Exception) {
                null
            }

            return response?.body?.string()
        }
    }


    fun changeBase(name: String): Fixer {
        val onePrice = BigDecimal("100").divide(rates[name], 2, RoundingMode.HALF_UP)
        println(onePrice)
        val newRates: MutableMap<String, BigDecimal> = mutableMapOf()
        rates.forEach { (rate, _) ->
            if (rate != name)
                newRates[rate] = rates[rate]!!.multiply(onePrice).divide(BigDecimal("100"))
        }

        newRates[base] = BigDecimal("1")
        return Fixer(timestamp, name, newRates)
    }
}
