package com.prorecek.catfacts.di

import com.prorecek.catfacts.domain.CatFactRepository
import com.prorecek.catfacts.domain.GetRandomCatFactUseCase
import com.prorecek.catfacts.infrastructure.CatFactApi
import com.prorecek.catfacts.infrastructure.CatFactRepositoryImpl
import com.prorecek.catfacts.presentation.viewmodel.CatFactsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val catFactModule = module {
    viewModel { CatFactsViewModel(get()) }

    factory { provideCatFactApi() }
    factory<CatFactRepository> { CatFactRepositoryImpl(get()) }
    factory { GetRandomCatFactUseCase(get()) }
}

private fun provideCatFactApi(): CatFactApi {
    return Retrofit
        .Builder()
        .baseUrl("https://cat-fact.herokuapp.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CatFactApi::class.java)
}
