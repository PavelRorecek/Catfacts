package com.prorecek.catfacts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prorecek.catfacts.entity.CatFact
import com.prorecek.catfacts.repository.CatFactsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CatFactsViewModel : ViewModel() {

    private val catFactsRepository = CatFactsRepository()

    private val _catFact = MutableLiveData<CatFact>()
    val catFact: LiveData<CatFact>
        get() = _catFact

    fun loadRandomCatFact() {
        viewModelScope.launch(Dispatchers.IO) {
            _catFact.postValue(catFactsRepository.getRandomCatFact())
        }
    }

}
