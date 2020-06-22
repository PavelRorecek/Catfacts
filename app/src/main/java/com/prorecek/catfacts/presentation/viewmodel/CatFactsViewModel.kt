package com.prorecek.catfacts.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prorecek.catfacts.domain.GetRandomCatFactUseCase
import com.prorecek.catfacts.domain.model.CatFact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CatFactsViewModel(
    private val getRandomCatFactUseCase: GetRandomCatFactUseCase
) : ViewModel() {

    private val _catFact = MutableStateFlow<CatFact?>(null)
    val catFact: StateFlow<CatFact?> = _catFact

    fun loadRandomCatFact() {
        viewModelScope.launch(Dispatchers.IO) {
            _catFact.value = getRandomCatFactUseCase()
        }
    }
}
