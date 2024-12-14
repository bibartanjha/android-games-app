package com.example.android_games_app.games.wordle.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_games_app.games.wordle.LetterGuessTextView
import com.example.android_games_app.games.wordle.TextInputButton
import com.example.android_games_app.games.wordle.WordleFixedValues.KEYBOARD_ROWS
import com.example.android_games_app.games.wordle.WordleFixedValues.NUM_LETTERS_IN_WORD
import com.example.android_games_app.games.wordle.WordleFixedValues.NUM_POSSIBLE_GUESSES
import com.example.android_games_app.games.wordle.viewmodel.WordleGameViewModel

@Composable
fun WordleGameScreen(
    wordleGameViewModel: WordleGameViewModel
) {
    val gameState by wordleGameViewModel.getWordleGameState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(red = 158, green = 210, blue = 198)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            for (row in 0 until NUM_POSSIBLE_GUESSES) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                ) {
                    for (col in 0 until NUM_LETTERS_IN_WORD) {
                        LetterGuessTextView(
                            bgColor = gameState.letterGuessValues[row][col].currentBGColor,
                            letterInView = gameState.letterGuessValues[row][col].currentLetter
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(40.dp))

            for (keyboardRow in KEYBOARD_ROWS.indices) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                ) {
                    if (keyboardRow == KEYBOARD_ROWS.size - 1) { // add BACK button to last row
                        TextInputButton(
                            textInButton = "BACK",
                            buttonClicked = {
                                wordleGameViewModel.removeLetter()
                            },
                            buttonWidth = 60.dp,
                            buttonHeight = 60.dp,
                            fontSize = 16.sp
                        )
                    }

                    for (letter in KEYBOARD_ROWS[keyboardRow]) {
                        TextInputButton(
                            textInButton = letter.toString(),
                            buttonClicked = {
                                wordleGameViewModel.addLetter(it)
                            }
                        )
                    }

                    if (keyboardRow == KEYBOARD_ROWS.size - 1) { // add ENTER button to last row
                        TextInputButton(
                            textInButton = "ENTER",
                            buttonClicked = {
                                wordleGameViewModel.handleEnter()
                            },
                            buttonWidth = 60.dp,
                            buttonHeight = 60.dp,
                            fontSize = 16.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Preview
@Composable
fun WordleGameScreenPreview() {
    WordleGameScreen(WordleGameViewModel())
}