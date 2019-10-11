package com.prorecek.catfacts

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.prorecek.catfacts.entity.CatFact
import com.prorecek.catfacts.viewmodel.CatFactStatus
import com.prorecek.catfacts.viewmodel.CatFactsViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var catFactsViewModel: CatFactsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        catFactsViewModel = ViewModelProviders.of(this).get(CatFactsViewModel::class.java)
        catFactsViewModel.getRandomCatFact().observe(this, CatFactStatusesObserver())

        pause_button.setOnTouchListener(PauseButtonOnTouchListener())
    }

    private fun hideCatFact() {
        ObjectAnimator
            .ofFloat(cat_fact, View.ALPHA, cat_fact.alpha, 0f)
            .apply {
                duration = 300
                startDelay = CatFactsViewModel.TICK_DURATION - 300
            }
            .start()
    }

    private fun updateProgressBar(progress: Int) {
        if (progress == 0) {
            periodic_load_progress.progress = 0
        } else {
            ObjectAnimator
                .ofInt(
                    periodic_load_progress,
                    "progress",
                    periodic_load_progress.progress,
                    progress
                )
                .apply {
                    duration = CatFactsViewModel.TICK_DURATION
                    interpolator = LinearInterpolator()
                }
                .start()
        }
    }

    private fun showCatFact(catFact: CatFact) {
        cat_fact.text = catFact.text

        ObjectAnimator
            .ofFloat(cat_fact, View.ALPHA, 0f, 1f)
            .apply { duration = 300 }
            .start()
    }

    private inner class CatFactStatusesObserver : Observer<List<CatFactStatus>> {
        override fun onChanged(catFactStatuses: List<CatFactStatus>?) {
            catFactStatuses?.forEach { catFactStatus ->
                when (catFactStatus) {
                    is CatFactStatus.Received -> {
                        showCatFact(catFactStatus.catFact)
                    }
                    CatFactStatus.Loading -> {
                        // TODO - show loading indicator
                    }
                    is CatFactStatus.RemainingTimeToRefresh -> {
                        updateProgressBar((catFactStatus.remainingTimeToRefresh * 100).toInt())

                        if (catFactStatus.remainingTimeToRefresh.toInt() == 1) {
                            hideCatFact()
                        }
                    }
                }
            }
        }
    }

    private inner class PauseButtonOnTouchListener : View.OnTouchListener {

        override fun onTouch(view: View, event: MotionEvent): Boolean {
            view.performClick()

            return when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    catFactsViewModel.pauseFactRefresh()
                    pause_button.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            android.R.drawable.ic_media_play,
                            null
                        )
                    )

                    true
                }
                MotionEvent.ACTION_UP -> {
                    catFactsViewModel.resumeFactRefresh()
                    pause_button.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            android.R.drawable.ic_media_pause,
                            null
                        )
                    )
                    true
                }
                else -> false
            }
        }

    }

}
