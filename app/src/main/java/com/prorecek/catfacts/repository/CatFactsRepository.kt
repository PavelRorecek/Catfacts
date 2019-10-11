package com.prorecek.catfacts.repository

import com.prorecek.catfacts.database.CatFactDatabase

class CatFactsRepository {

    private val catFactDatabase = CatFactDatabase()

    suspend fun getRandomCatFact() = catFactDatabase.getRandomCatFact()

}
