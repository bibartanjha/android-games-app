package com.example.android_games_app.games.frogger

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android_games_app.R
import com.example.android_games_app.games.frogger.utils.Frog
import com.example.android_games_app.games.frogger.FroggerFixedValues.FROGGER_SCREEN_BG_COLOR
import com.example.android_games_app.games.frogger.FroggerFixedValues.columnWidth
import com.example.android_games_app.games.frogger.FroggerFixedValues.gameRows
import com.example.android_games_app.games.frogger.FroggerFixedValues.rowAmountOfScreen
import com.example.android_games_app.games.frogger.FroggerFixedValues.rowAnimCounterInterval
import com.example.android_games_app.games.frogger.FroggerFixedValues.rowHeight
import com.example.android_games_app.games.frogger.FroggerFixedValues.topBarAmountOfScreen
import com.example.android_games_app.games.frogger.FroggerFixedValues.topBarHeight
import com.example.android_games_app.games.frogger.utils.RowObject
import com.example.android_games_app.navigation.Routes
import com.example.android_games_app.utils.DirectionButtons
import com.example.android_games_app.utils.GamePauseOrCompleteScreen
import com.example.android_games_app.utils.GameProgressStatus
import com.example.android_games_app.utils.TopBarWithBackIcon

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FroggerScreen(
    froggerViewModel: FroggerViewModel,
    onNavigateToOtherScreen: (optionName: String) -> Unit,
) {
    val gameState by froggerViewModel.getFroggerGameState.collectAsState()

    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp.toFloat()
    val screenHeight = configuration.screenHeightDp.toFloat()

    var screenWidthDependentValuesAreSet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!screenWidthDependentValuesAreSet) {
            froggerViewModel.setValuesBasedOnScreenSize(screenWidth, screenHeight)
            screenWidthDependentValuesAreSet = true
        }
    }

    Scaffold(
        topBar = {
            TopBarWithBackIcon(
                gameName = "Frogger",
                onIconClicked = {
                    if (froggerViewModel.getFroggerGameState.value.gameProgressStatus == GameProgressStatus.IN_PROGRESS) {
                        froggerViewModel.pauseGame()
                    }
                    onNavigateToOtherScreen(Routes.HOME_SCREEN)
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = FROGGER_SCREEN_BG_COLOR
        ) {
            // The basic background:
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth()
                        .height((screenHeight * topBarAmountOfScreen).dp)
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = { froggerViewModel.pauseGame() },
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Text("⏸")
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(topBarHeight.dp)
                        .paint(
                            painterResource(id = R.drawable.frogger_background_endzone),
                            contentScale = ContentScale.FillBounds
                        )
                )
                repeat(5) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height((screenHeight * rowAmountOfScreen).dp)
                            .paint(
                                painterResource(id = R.drawable.frogger_background_river_row),
                                contentScale = ContentScale.FillBounds
                            )
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((screenHeight * rowAmountOfScreen).dp)
                        .paint(
                            painterResource(id = R.drawable.frogger_background_midzone),
                            contentScale = ContentScale.FillBounds
                        )
                )
                repeat(5) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height((screenHeight * rowAmountOfScreen).dp)
                            .background(Color.Black)
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((screenHeight * rowAmountOfScreen).dp)
                        .paint(
                            painterResource(id = R.drawable.frogger_background_startzone),
                            contentScale = ContentScale.FillBounds
                        )
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) {
                    if (screenWidthDependentValuesAreSet) {
                        // The road objects and river objects:
                        for (rowNumber in 0 until gameRows.size) {
                            for (objectIndex in 0 until gameRows[rowNumber].objectsInLane.size) {
                                val objectType = gameRows[rowNumber].objectsInLane[objectIndex]
                                val objectAnimationCounter = (gameState.rowObjectAnimCounter / rowAnimCounterInterval)
                                val obstacleImageResourceValue = RowObject.objectToImageMap[objectType]?.get(objectAnimationCounter)
                                val obstacleImagePainter = if (obstacleImageResourceValue == null) {
                                    painterResource(id = RowObject.getDefaultObjectImage())
                                } else {
                                    painterResource(id = obstacleImageResourceValue)
                                }
                                val obstacleXOffset = gameState.objectXOffsets[rowNumber][objectIndex]
                                val obstacleWidth = columnWidth * gameRows[rowNumber].numColumnsTakenUpByEachObject

                                Image(
                                    modifier = Modifier
                                        .width(obstacleWidth.dp)
                                        .height(rowHeight.dp)
                                        .padding(start = 0.dp)
                                        .offset(
                                            x = obstacleXOffset.dp,
                                            y = gameRows[rowNumber].yOffsetValueForRow.dp
                                        ),
                                    painter = obstacleImagePainter,
                                    contentDescription = null,
                                )

                            }
                        }

                        // The frog:
                        val frogImageResourceValue = Frog.statusToImageMap[gameState.frogDisplayStatus]
                        val frogImagePainter = if (frogImageResourceValue == null) {
                            painterResource(id = Frog.getDefaultDirectionImage())
                        } else {
                            painterResource(id = frogImageResourceValue)
                        }

                        Image(
                            modifier = Modifier
                                .width(columnWidth.dp)
                                .height(rowHeight.dp)
                                .padding(start = 0.dp)
                                .offset(
                                    x = gameState.frogXOffset.dp,
                                    y = gameRows[gameState.frogCurrentRowIndex].yOffsetValueForRow.dp
                                ),
                            painter = frogImagePainter,
                            contentDescription = null,
                        )
                    }
                }

                val buttonSize = (screenWidth/7).toInt()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = buttonSize.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))

                    DirectionButtons(
                        arrowCardWidth = buttonSize,
                        onUpClicked = {
                            froggerViewModel.onFrogVerticalMovement(goingUp = true)
                        },
                        onDownClicked = {
                            froggerViewModel.onFrogVerticalMovement(goingUp = false)
                        },
                        onLeftClicked = {
                            froggerViewModel.onFrogHorizontalMovement(
                                goingLeft = true,
                                screenWidth = screenWidth
                            )
                        },
                        onRightClicked = {
                            froggerViewModel.onFrogHorizontalMovement(
                                goingLeft = false,
                                screenWidth = screenWidth
                            )
                        }
                    )
                }
            }


            if (gameState.gameProgressStatus == GameProgressStatus.PAUSED) {
                GamePauseOrCompleteScreen(
                    text = "Game Paused",
                    cardBGColor = Color.LightGray,
                    textColor = Color.Black,
                    buttonTexts = listOf("Resume", "Restart"),
                    onButtonSelection = {
                        if (it == "Resume") {
                            froggerViewModel.resumeGame()
                        } else if (it == "Restart") {
                            froggerViewModel.startNewGame()
                        }
                    }
                )
            } else if (gameState.gameProgressStatus in listOf(GameProgressStatus.LOST, GameProgressStatus.WON)) {
                GamePauseOrCompleteScreen(
                    text = if (gameState.gameProgressStatus == GameProgressStatus.LOST) {
                        "Game Over"
                    } else {
                        "Congrats!"
                    },
                    cardBGColor = Color.LightGray,
                    textColor = Color.Black,
                    buttonTexts = listOf("Restart Game", "Return to Main Menu"),
                    onButtonSelection = {
                        if (it == "Restart Game") {
                            froggerViewModel.startNewGame()
                        } else if (it == "Return to Main Menu") {
                            onNavigateToOtherScreen(Routes.HOME_SCREEN)
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun FroggerScreenPreview() {
    FroggerScreen(
        froggerViewModel = FroggerViewModel(),
        onNavigateToOtherScreen = {}
    )
}