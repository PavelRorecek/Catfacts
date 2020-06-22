package com.prorecek.catfacts.infrastructure

import com.prorecek.catfacts.domain.CatFactRepository
import com.prorecek.catfacts.domain.model.CatFact

class CatFactRepositoryImpl(
    private val catFactApi: CatFactApi
) : CatFactRepository {

    override suspend fun getRandomCatFact(): CatFact {
        return catFactApi.getRandomCatFact()
    }
}
