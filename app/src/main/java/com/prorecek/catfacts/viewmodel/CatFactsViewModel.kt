package com.prorecek.catfacts.viewmodel

import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prorecek.catfacts.entity.CatFact
import com.prorecek.catfacts.repository.CatFactsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CatFactsViewModel : ViewModel() {

    private val catFactsRepository = CatFactsRepository()
    private val catFactStatuses = MutableLiveData<List<CatFactStatus>>()
    private val factRefresher: FactRefresher = FactRefresher(
        catFactsRepository,
        catFactStatuses,
        TICK_DURATION,
        viewModelScope
    )

    init {
        factRefresher.start()
    }

    override fun onCleared() {
        super.onCleared()
        factRefresher.stop()
    }

    fun getRandomCatFact(): LiveData<List<CatFactStatus>> = catFactStatuses

    fun pauseFactRefresh() {
        factRefresher.paused = true
    }

    fun resumeFactRefresh() {
        factRefresher.paused = false
    }

    companion object {
        const val TICK_DURATION = 150L
    }
}

typealias Percent = Float

sealed class CatFactStatus {
    class Received(val catFact: CatFact) : CatFactStatus()
    object Loading : CatFactStatus()
    class RemainingTimeToRefresh(val remainingTimeToRefresh: Percent) : CatFactStatus()
}

class FactRefresher(
    catFactsRepository: CatFactsRepository,
    catFact: MutableLiveData<List<CatFactStatus>>,
    val tickDuration: Long,
    viewModelScope: CoroutineScope
) : Handler() {

    private val refresher: Runnable
    var paused = false

    init {
        refresher = object : Runnable {
            val that = this
            var ticks = 0
            val refreshTick = 25

            override fun run() {
                Log.d("test", "$ticks")

                val catFactStatusesToPost = mutableListOf<CatFactStatus>()

                viewModelScope.launch(Dispatchers.IO) {
                    if (!paused) {
                        if (ticks == refreshTick) {
                            ticks = 0
                        }

                        catFactStatusesToPost.add(
                            CatFactStatus.RemainingTimeToRefresh(ticks / refreshTick.toFloat())
                        )

                        when (ticks) {
                            0 -> {
                                catFactStatusesToPost.add(
                                    CatFactStatus.Received(catFactsRepository.getRandomCatFact())
                                )
                                ticks++
                            }
                            else -> ticks++
                        }

                        catFact.postValue(catFactStatusesToPost)
                    }
                    this@FactRefresher.postDelayed(that, tickDuration)
                }
            }
        }
    }

    fun start() {
        refresher.run()
    }

    fun stop() {
        removeCallbacks(refresher)
    }

}
