package com.prorecek.catfacts.infrastructure

import com.prorecek.catfacts.domain.model.CatFact
import retrofit2.http.GET

interface CatFactApi {

    @GET("facts/random")
    suspend fun getRandomCatFact(): CatFact
}
