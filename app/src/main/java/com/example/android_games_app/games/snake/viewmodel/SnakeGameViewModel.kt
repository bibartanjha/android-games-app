package com.example.android_games_app.games.snake.viewmodel

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_games_app.games.snake.model.SnakeGameState
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SnakeGameViewModel: ViewModel() {

    private val snakeGameState = MutableStateFlow(SnakeGameState())
    val getSnakeGameState: StateFlow<SnakeGameState> = snakeGameState

    private val handler = Handler(Looper.getMainLooper())

    private var isGameLoopRunning = false

    private val gameLoopRunnable = object : Runnable {
        override fun run() {
            if (!snakeGameState.value.isPaused) {
                val currentNumber = snakeGameState.value.currentNumber
                snakeGameState.value = snakeGameState.value.copy(
                    currentNumber = currentNumber + 1
                )
                handler.postDelayed(this, 200)
            }
        }
    }

    init {
        observeIsPausedState()
    }

    private fun observeIsPausedState() {
        viewModelScope.launch {
            getSnakeGameState.collect { state ->
                if (!state.isPaused) {
                    startGameLoop()
                } else {
                    stopGameLoop()
                }
            }
        }
    }

    private fun startGameLoop() {
        if (isGameLoopRunning) {
            return
        }
        isGameLoopRunning = true
        handler.post(gameLoopRunnable)
    }

    private fun stopGameLoop() {
        isGameLoopRunning = false
        handler.removeCallbacks(gameLoopRunnable)
    }

    fun updateIsPaused(isPaused: Boolean) {
        snakeGameState.value = snakeGameState.value.copy(
            isPaused = isPaused
        )
    }

    fun restartGame() {
        stopGameLoop()
        snakeGameState.value = snakeGameState.value.copy(
            isPaused = true,
            currentNumber = 0
        )
    }

    fun getCurrentNumber() = snakeGameState.value.currentNumber
}