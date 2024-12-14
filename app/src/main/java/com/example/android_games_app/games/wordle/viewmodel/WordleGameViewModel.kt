package com.example.android_games_app.games.wordle.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.android_games_app.games.wordle.WordleFixedValues.NUM_LETTERS_IN_WORD
import com.example.android_games_app.games.wordle.WordleFixedValues.NUM_POSSIBLE_GUESSES
import com.example.android_games_app.games.wordle.model.LetterGuess
import com.example.android_games_app.games.wordle.model.WordleGameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WordleGameViewModel : ViewModel() {
    private val wordleGameState = MutableStateFlow(WordleGameState())
    val getWordleGameState: StateFlow<WordleGameState> = wordleGameState

    private fun setNewWord() {
        val newWord = "ARISE" // I will replace with API call later

        val lettersPlacements: MutableMap<Char, MutableSet<Int>> = HashMap()
        for (i in newWord.indices) {
            val letter = newWord[i]
            lettersPlacements.computeIfAbsent(letter) { HashSet() }.add(i)
        }

        wordleGameState.value = wordleGameState.value.copy(
            wordForUserToGuess = newWord,
            lettersPlacements = lettersPlacements
        )
    }

    private fun getGuessValidity(
        userInput: String
    ): MutableList<Int> {
        val correctWordLetterPlacements = wordleGameState.value.lettersPlacements

        val resultsOfUserInput: MutableList<Int> = mutableListOf()
        for (i in 0 until NUM_LETTERS_IN_WORD) {
            val letter = userInput[i]
            if (correctWordLetterPlacements.containsKey(letter)) {
                if (correctWordLetterPlacements[letter]?.contains(i) == true) {
                    resultsOfUserInput.add(2)
                } else {
                    resultsOfUserInput.add(1)
                }
            } else {
                resultsOfUserInput.add(0)
            }
        }
        return resultsOfUserInput
    }

    private fun updateListWithNewLetter(
        rowToUpdate: Int,
        colToUpdate: Int,
        newLetter: String
    ): List<MutableList<LetterGuess>> {
        val gameState = wordleGameState.value
        val updatedLetterGuessValues = gameState.letterGuessValues.mapIndexed { rowIndex, row ->
            if (rowIndex == rowToUpdate) {
                row.mapIndexed { colIndex, guess ->
                    if (colIndex == colToUpdate) {
                        guess.copy(currentLetter = newLetter)
                    } else {
                        guess
                    }
                }.toMutableList()
            } else {
                row.toMutableList()
            }
        }
        return updatedLetterGuessValues
    }

    fun addLetter(newLetter: String) {
        val gameState = wordleGameState.value
        val currentGuessLetterIndex = gameState.currentGuessLetterIndex
        if (currentGuessLetterIndex < NUM_LETTERS_IN_WORD) {
            val updatedLetterGuessList = updateListWithNewLetter(
                rowToUpdate = gameState.currentGuessNumber,
                colToUpdate = gameState.currentGuessLetterIndex,
                newLetter
            )
            wordleGameState.value = gameState.copy(
                currentGuessLetterIndex = currentGuessLetterIndex + 1,
                letterGuessValues = updatedLetterGuessList
            )
        }
    }

    fun removeLetter() {
        val gameState = wordleGameState.value
        val currentGuessLetterIndex = gameState.currentGuessLetterIndex
        if (currentGuessLetterIndex > 0) {
            val updatedLetterGuessList = updateListWithNewLetter(
                rowToUpdate = gameState.currentGuessNumber,
                colToUpdate = gameState.currentGuessLetterIndex - 1,
                ""
            )
            wordleGameState.value = gameState.copy(
                currentGuessLetterIndex = currentGuessLetterIndex - 1,
                letterGuessValues = updatedLetterGuessList
            )
        }
    }

    fun handleEnter() {
        val gameState = wordleGameState.value
        if (gameState.currentGuessLetterIndex == NUM_LETTERS_IN_WORD) {
            val userSubmittedWord = gameState.letterGuessValues[gameState.currentGuessNumber]
                .joinToString("") { it.currentLetter }
            val guessValidity = getGuessValidity(userSubmittedWord)

            val updatedLetterGuessValues = gameState.letterGuessValues.mapIndexed { rowIndex, row ->
                if (rowIndex == gameState.currentGuessNumber) {
                    row.mapIndexed { colIndex, guess ->
                        if (guessValidity[colIndex] == 0) {
                            guess.copy(currentBGColor = Color.DarkGray)
                        } else if (guessValidity[colIndex] == 1) {
                            guess.copy(currentBGColor = Color.Yellow)
                        } else {
                            guess.copy(currentBGColor = Color.Green)
                        }
                    }.toMutableList()
                } else {
                    row.toMutableList()
                }
            }

            val currentGuessNumber = gameState.currentGuessNumber
            val currentGuessLetterIndex = gameState.currentGuessLetterIndex

            wordleGameState.value = gameState.copy(
                letterGuessValues = updatedLetterGuessValues,
                currentGuessNumber = if (currentGuessNumber == (NUM_POSSIBLE_GUESSES - 1)) {
                    currentGuessNumber
                } else {
                    currentGuessNumber + 1
                },
                currentGuessLetterIndex = if (currentGuessNumber == (NUM_POSSIBLE_GUESSES - 1)) {
                    currentGuessLetterIndex
                } else {
                    0
                }
            )
        }
    }

    fun startNewGame() {
        setNewWord()
    }
}