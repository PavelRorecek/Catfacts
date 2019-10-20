package com.prorecek.catfacts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.prorecek.catfacts.entity.CatFact
import com.prorecek.catfacts.viewmodel.CatFactsViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var catFactAnimator: CatFactAnimator
    private lateinit var catFactsViewModel: CatFactsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        catFactsViewModel = ViewModelProviders
            .of(this)
            .get(CatFactsViewModel::class.java)
        catFactsViewModel.catFact.observe(this, Observer<CatFact> { showCatFact(it) })

        catFactAnimator = CatFactAnimator(
            catFactText = cat_fact,
            progressBar = periodic_load_progress,
            refreshInterval = 5000,
            onProgressBarMaximumValue = { catFactsViewModel.loadRandomCatFact() }
        )

        pause_button.setOnTouchListener(
            PauseButtonListener(
                pauseButton = pause_button,
                pauseIcon = ResourcesCompat.getDrawable(
                    resources,
                    android.R.drawable.ic_media_pause,
                    null
                )!!,
                playIcon = ResourcesCompat.getDrawable(
                    resources,
                    android.R.drawable.ic_media_play,
                    null
                )!!,
                onDown = { catFactAnimator.pause() },
                onUp = { catFactAnimator.resume() }
            )
        )

        catFactsViewModel.loadRandomCatFact()
    }

    override fun onDestroy() {
        super.onDestroy()

        catFactAnimator.stop()
    }

    private fun showCatFact(catFact: CatFact) {
        cat_fact.text = catFact.text

        catFactAnimator.start()
    }

}
