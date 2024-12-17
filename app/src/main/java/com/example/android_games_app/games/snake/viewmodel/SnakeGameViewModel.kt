package com.example.android_games_app.games.snake.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_games_app.games.snake.model.SnakeGameState
import kotlinx.coroutines.Job
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SnakeGameViewModel: ViewModel() {

    private val snakeGameState = MutableStateFlow(SnakeGameState())
    val getSnakeGameState: StateFlow<SnakeGameState> = snakeGameState
    private var job: Job? = null

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
        // If the game loop is already running, don't start it again.
        if (job != null && job?.isActive == true) return

        job = viewModelScope.launch {
            while (!snakeGameState.value.isPaused) {
                delay(1000) // Delay for 1 second
                val currentNumber = snakeGameState.value.currentNumber
                snakeGameState.value = snakeGameState.value.copy(currentNumber = currentNumber + 1)
            }
        }
    }

//    private fun stopGameLoop() {
//        isGameLoopRunning = false
//        handler.removeCallbacks(gameLoopRunnable)
//    }

    private fun stopGameLoop() {
        job?.cancel() // Cancel the current job if it is running
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