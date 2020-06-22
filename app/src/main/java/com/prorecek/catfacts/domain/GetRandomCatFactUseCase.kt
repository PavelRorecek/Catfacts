package com.prorecek.catfacts.domain

import com.prorecek.catfacts.domain.model.CatFact

class GetRandomCatFactUseCase(
    private val catFactRepository: CatFactRepository
) {

    suspend operator fun invoke(): CatFact {
        return catFactRepository.getRandomCatFact()
    }
}
