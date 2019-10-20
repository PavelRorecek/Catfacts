package com.prorecek.catfacts.service

import com.prorecek.catfacts.entity.CatFact
import retrofit2.Call
import retrofit2.http.GET

interface CatFactService {

    @GET("facts/random")
    fun getRandomCatFact(): Call<CatFact>

}
