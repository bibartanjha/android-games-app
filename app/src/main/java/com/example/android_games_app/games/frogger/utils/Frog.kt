package com.example.android_games_app.games.frogger.utils

import com.example.android_games_app.R

object Frog {
    enum class FrogDirection {
        DOWN,
        UP,
        LEFT,
        RIGHT
    }

    val directionToImageMap = hashMapOf(
        FrogDirection.LEFT to R.drawable.froggerleft,
        FrogDirection.RIGHT to R.drawable.froggerright,
        FrogDirection.UP to R.drawable.froggerup,
        FrogDirection.DOWN to R.drawable.froggerdown,
    )

    fun getDefaultDirectionImage(): Int = R.drawable.froggerup
}