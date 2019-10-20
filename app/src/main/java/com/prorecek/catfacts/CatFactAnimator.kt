package com.prorecek.catfacts

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.animation.doOnEnd

typealias Millis = Int

class CatFactAnimator(
    catFactText: TextView,
    progressBar: ProgressBar,
    refreshInterval: Millis,
    onProgressBarMaximumValue: () -> Unit
) {

    private val progressBarAnimator = ObjectAnimator
        .ofInt(
            progressBar,
            "progress",
            0,
            100
        )
        .apply {
            duration = refreshInterval.toLong()
            interpolator = LinearInterpolator()
            doOnEnd { onProgressBarMaximumValue() }
        }

    private val catFactFadeInAnimator = ObjectAnimator
        .ofFloat(catFactText, View.ALPHA, 0f, 1f)
        .apply { duration = 300 }

    private val catFactDelayedFadeOutAnimator = ObjectAnimator
        .ofFloat(catFactText, View.ALPHA, 1f, 0f)
        .apply {
            duration = 300
            startDelay = refreshInterval.toLong() - 300
        }

    private val animatorSet = AnimatorSet()

    fun start() {
        animatorSet
            .apply {
                playTogether(
                    catFactFadeInAnimator,
                    progressBarAnimator,
                    catFactDelayedFadeOutAnimator
                )
            }
            .start()
    }

    fun pause() {
        animatorSet.pause()
    }

    fun resume() {
        animatorSet.resume()
    }

    fun stop() {
        animatorSet.cancel()
    }

}
