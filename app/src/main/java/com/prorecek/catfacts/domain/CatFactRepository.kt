package com.prorecek.catfacts.domain

import com.prorecek.catfacts.domain.model.CatFact

interface CatFactRepository {
    suspend fun getRandomCatFact(): CatFact
}
