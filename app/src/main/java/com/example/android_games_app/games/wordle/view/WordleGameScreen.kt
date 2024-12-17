package com.example.android_games_app.games.wordle.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_games_app.games.ui.PostGameOptions
import com.example.android_games_app.games.ui.TextViewWithMessages
import com.example.android_games_app.games.wordle.utils.WordleFixedValues.KEYBOARD_ROWS
import com.example.android_games_app.games.wordle.utils.WordleFixedValues.NUM_LETTERS_IN_WORD
import com.example.android_games_app.games.wordle.utils.WordleFixedValues.NUM_POSSIBLE_GUESSES
import com.example.android_games_app.games.wordle.utils.WordleFixedValues.WORDLE_SCREEN_BG_COLOR
import com.example.android_games_app.games.wordle.viewmodel.WordleGameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordleGameScreen(
    wordleGameViewModel: WordleGameViewModel,
    onPostGameOptionSelected: (optionName: String) -> Unit,
    onBackClicked: () -> Unit
) {
    val gameState by wordleGameViewModel.getWordleGameState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Wordle")
                },
                navigationIcon = {
                    IconButton(onClick = {onBackClicked()}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                )
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues),
            color = WORDLE_SCREEN_BG_COLOR
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val configuration = LocalConfiguration.current
                val screenWidth = configuration.screenWidthDp
                val screenHeight = configuration.screenHeightDp

                Spacer(modifier = Modifier.height(10.dp))
                for (row in 0 until NUM_POSSIBLE_GUESSES) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                    ) {
                        for (col in 0 until NUM_LETTERS_IN_WORD) {
                            LetterGuessTextView(
                                backgroundColor = gameState.letterGuessValues[row][col].currentBGColor,
                                letterInView = gameState.letterGuessValues[row][col].currentLetter,
                                boxSize = if (gameState.gameInProgress) {
                                    (screenWidth/5.5).toInt()
                                } else {
                                    (screenWidth/8.22).toInt()
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (gameState.gameInProgress) {
                    Spacer(modifier = Modifier.height(20.dp))
                    for (keyboardRow in KEYBOARD_ROWS.indices) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                        ) {
                            val specialButtonDimension = (screenWidth/6.85).dp
                            val specialButtonFontSize = (screenWidth/25.68).sp
                            if (keyboardRow == KEYBOARD_ROWS.size - 1) { // add BACK button to last row
                                TextInputButton(
                                    textInButton = "BACK",
                                    buttonClicked = {
                                        wordleGameViewModel.removeLetter()
                                    },
                                    buttonWidth = specialButtonDimension,
                                    buttonHeight = specialButtonDimension,
                                    fontSize = specialButtonFontSize
                                )
                            }

                            for (letter in KEYBOARD_ROWS[keyboardRow]) {
                                TextInputButton(
                                    textInButton = letter.toString(),
                                    buttonClicked = {
                                        wordleGameViewModel.addLetter(it)
                                    },
                                    buttonWidth = (screenWidth/12.8).dp,
                                    buttonHeight = (screenHeight/13.125).dp,
                                    fontSize = (screenWidth/15.74).sp
                                )
                            }

                            if (keyboardRow == KEYBOARD_ROWS.size - 1) { // add ENTER button to last row
                                TextInputButton(
                                    textInButton = "ENTER",
                                    buttonClicked = {
                                        wordleGameViewModel.handleEnter()
                                    },
                                    buttonWidth = specialButtonDimension,
                                    buttonHeight = specialButtonDimension,
                                    fontSize = specialButtonFontSize
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
                else {
                    Spacer(modifier = Modifier.height(10.dp))
                    TextViewWithMessages(
                        text = if (gameState.guessedWordSuccessfully) {
                            "Congrats!"
                        } else {
                            "The correct word was ${gameState.wordForUserToGuess}"
                        },
                        subTexts = listOf("Pick an option below"),
                        backgroundColor = Color.DarkGray,
                        textColor = Color.White,
                    )
                    PostGameOptions(
                        onPostGameOptionSelected = onPostGameOptionSelected
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun WordleGameScreenPreview() {
    WordleGameScreen(
        wordleGameViewModel = WordleGameViewModel(),
        onPostGameOptionSelected = {},
        onBackClicked = {}
    )
}