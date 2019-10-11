package com.prorecek.catfacts.database

import com.prorecek.catfacts.entity.CatFact
import com.prorecek.catfacts.service.CatFactService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CatFactDatabase {
    private val catFactService = Retrofit
        .Builder()
        .baseUrl("https://cat-fact.herokuapp.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CatFactService::class.java)

    suspend fun getRandomCatFact(): CatFact {
        return coroutineScope {
            repeat(5) {
                val randomCatFact = catFactService.getRandomCatFact()

                try {
                    val response = randomCatFact.execute().body()
                    response?.let { catFact ->
                        if (catFact.text.length in 20..300) return@coroutineScope response!!
                    }
                } catch (e: Exception) {
                    return@repeat
                }
            }

            CatFact("Unable to get cat fact. Check your internet connection")
        }
    }
}

