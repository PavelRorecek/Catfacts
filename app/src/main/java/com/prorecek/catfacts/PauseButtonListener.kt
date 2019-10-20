package com.prorecek.catfacts

import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PauseButtonListener(
    private val pauseButton: FloatingActionButton,
    private val pauseIcon: Drawable,
    private val playIcon: Drawable,
    private val onDown: () -> Unit,
    private val onUp: () -> Unit
) : View.OnTouchListener {

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        view.performClick()

        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                pauseButton.setImageDrawable(playIcon)
                onDown()
                true
            }
            MotionEvent.ACTION_UP -> {
                pauseButton.setImageDrawable(pauseIcon)
                onUp()
                true
            }
            else -> false
        }
    }

}
