package com.prorecek.catfacts.presentation

import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.prorecek.catfacts.R
import com.prorecek.catfacts.domain.model.CatFact
import com.prorecek.catfacts.presentation.viewmodel.CatFactsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var catFactAnimator: CatFactAnimator
    private val viewModel: CatFactsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            viewModel.catFact.collect {
                it?.let { fact -> showCatFact(fact) }
            }
        }

        catFactAnimator = CatFactAnimator(
            catFactText = cat_fact,
            progressBar = periodic_load_progress,
            refreshInterval = 5000,
            onProgressBarMaximumValue = { viewModel.loadRandomCatFact() }
        )

        scroll_view.setOnTouchListener { view, event ->
            view.performClick()

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    catFactAnimator.pause()
                    true
                }
                MotionEvent.ACTION_UP -> {
                    catFactAnimator.resume()
                    true
                }
                else -> false
            }
        }

        viewModel.loadRandomCatFact()
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
