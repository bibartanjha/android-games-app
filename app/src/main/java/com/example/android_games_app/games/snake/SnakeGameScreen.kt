package com.example.android_games_app.games.snake

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android_games_app.games.snake.utils.SnakeDirection
import com.example.android_games_app.games.snake.utils.SnakeFixedValues.NUM_GRID_COLS
import com.example.android_games_app.games.snake.utils.SnakeFixedValues.NUM_GRID_ROWS
import com.example.android_games_app.games.snake.utils.SnakeFixedValues.SNAKE_GAME_SCREEN_BG_COLOR
import com.example.android_games_app.navigation.Routes
import com.example.android_games_app.utils.DirectionButtons
import com.example.android_games_app.utils.OverlayMenuScreen
import com.example.android_games_app.utils.GameProgressStatus
import com.example.android_games_app.utils.TopBarWithBackIcon

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SnakeGameScreen(
    snakeGameViewModel: SnakeGameViewModel,
    onNavigateToOtherScreen: (optionName: String) -> Unit,
) {
    val gameState by snakeGameViewModel.getSnakeGameState.collectAsState()

    Scaffold(
        topBar = {
            TopBarWithBackIcon(
                gameName = "Snake",
                onIconClicked = {
                    if (snakeGameViewModel.getSnakeGameState.value.gameProgressStatus == GameProgressStatus.IN_PROGRESS) {
                        snakeGameViewModel.pauseGame()
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
            color = SNAKE_GAME_SCREEN_BG_COLOR
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val configuration = LocalConfiguration.current
                val screenWidth = configuration.screenWidthDp
                val screenHeight = configuration.screenHeightDp

                Spacer(modifier = Modifier.height(10.dp))

                Row (
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = { snakeGameViewModel.pauseGame() }
                    ) {
                        Text("⏸")
                    }
                }

                val textMeasurer = rememberTextMeasurer()

                Canvas(
                    modifier = Modifier
                        .width(screenWidth.dp)
                        .height((screenHeight*.6).dp)
                ) {
                    val canvasWidth = size.width

                    val dimensionGridCell = canvasWidth/25

                    val totalWidthOfGameBoard = dimensionGridCell * NUM_GRID_COLS
                    val totalHeightOfGameBoard = dimensionGridCell * NUM_GRID_ROWS

                    val horizontalMarginAroundGameBoard = (canvasWidth - totalWidthOfGameBoard) / 2
                    val verticalMargin = 10.toFloat()
                    val heightOfScoreRow = totalHeightOfGameBoard * .05f

                    val startOfGameBoard = heightOfScoreRow + verticalMargin

                    for (row in 0 until NUM_GRID_ROWS) {
                        for (col in 0 until NUM_GRID_COLS) {
                            drawRect(
                                color = Color.Gray,
                                size = androidx.compose.ui.geometry.Size(dimensionGridCell, dimensionGridCell),
                                topLeft = androidx.compose.ui.geometry.Offset(
                                    horizontalMarginAroundGameBoard + (col * dimensionGridCell),
                                    startOfGameBoard + (row * dimensionGridCell)
                                ),
                                style = Stroke(width = 1.dp.toPx())
                            )
                        }
                    }

                    val snakeCoordinates = gameState.snakeCoordinates

                    /**
                     * Note to self: doing this backwards so that the head gets drawn last
                     * That way, if the snake collides with itself, then the head (or index 0) color still gets shown
                     */
                    for (snakePointIndex in (snakeCoordinates.size - 1) downTo 0) {
                        var color = Color(red = 14, green = 152, blue = 12, alpha = 255)
                        if (snakePointIndex == 0) {
                            color = Color(red = 9, green = 100, blue = 8, alpha = 255)
                        }

                        drawRect(
                            color = color,
                            size = androidx.compose.ui.geometry.Size(dimensionGridCell, dimensionGridCell),
                            topLeft = androidx.compose.ui.geometry.Offset(
                                horizontalMarginAroundGameBoard + (snakeCoordinates[snakePointIndex].col * dimensionGridCell),
                                startOfGameBoard + (snakeCoordinates[snakePointIndex].row * dimensionGridCell)
                            ),
                        )
                    }

                    drawRect(
                        color = Color(red = 222, green = 196, blue = 55, alpha = 255),
                        size = androidx.compose.ui.geometry.Size(dimensionGridCell, dimensionGridCell),
                        topLeft = androidx.compose.ui.geometry.Offset(
                            horizontalMarginAroundGameBoard + (gameState.snakeFoodCoordinates.col * dimensionGridCell),
                            startOfGameBoard + (gameState.snakeFoodCoordinates.row * dimensionGridCell)
                        ),
                    )

                    drawRect(
                        color = Color.White,
                        size = androidx.compose.ui.geometry.Size(totalWidthOfGameBoard, totalHeightOfGameBoard),
                        topLeft = androidx.compose.ui.geometry.Offset(horizontalMarginAroundGameBoard, startOfGameBoard),
                        style = Stroke(width = 4.dp.toPx())
                    )

                    drawRect(
                        color = Color.White,
                        size = androidx.compose.ui.geometry.Size(totalWidthOfGameBoard, heightOfScoreRow),
                        topLeft = androidx.compose.ui.geometry.Offset(horizontalMarginAroundGameBoard, verticalMargin),
                        style = Stroke(width = 4.dp.toPx())
                    )

                    drawText(
                        textMeasurer = textMeasurer,
                        text = "Score: ${gameState.currentScore}",
                        style = TextStyle(
                            fontWeight = FontWeight.W800,
                            color = Color.White
                        ),
                        topLeft = androidx.compose.ui.geometry.Offset(
                            horizontalMarginAroundGameBoard + (totalWidthOfGameBoard * .02f),
                            verticalMargin + (heightOfScoreRow * .1f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))
                DirectionButtons(
                    arrowCardWidth = (screenWidth/7),
                    onUpClicked = {
                        snakeGameViewModel.updateSnakeDirection(SnakeDirection.UP)
                    },
                    onDownClicked = {
                        snakeGameViewModel.updateSnakeDirection(SnakeDirection.DOWN)
                    },
                    onLeftClicked = {
                        snakeGameViewModel.updateSnakeDirection(SnakeDirection.LEFT)
                    },
                    onRightClicked = {
                        snakeGameViewModel.updateSnakeDirection(SnakeDirection.RIGHT)
                    }
                )
            }
            if (gameState.gameProgressStatus == GameProgressStatus.PAUSED) {
                OverlayMenuScreen(
                    text = "Game Paused",
                    cardBGColor = Color.LightGray,
                    textColor = Color.Black,
                    buttonTexts = listOf("Resume", "Restart"),
                    onButtonSelection = {
                        if (it == "Resume") {
                            snakeGameViewModel.resumeGame()
                        } else if (it == "Restart") {
                            snakeGameViewModel.startNewGame()
                        }
                    }
                )
            } else if (gameState.gameProgressStatus == GameProgressStatus.SHOWING_CUSTOMIZATION_SCREEN) {
                OverlayMenuScreen(
                    text = "Game Paused",
                    cardBGColor = Color.LightGray,
                    textColor = Color.Black,
                    buttonTexts = listOf("Resume", "Restart"),
                    onButtonSelection = {
                        if (it == "Resume") {
                            snakeGameViewModel.resumeGame()
                        } else if (it == "Restart") {
                            snakeGameViewModel.startNewGame()
                        }
                    }
                )
            } else if (gameState.gameProgressStatus in listOf(GameProgressStatus.LOST, GameProgressStatus.WON)) {
                OverlayMenuScreen(
                    text = "Game Over!",
                    subTexts = listOf("You scored ${gameState.currentScore} points"),
                    cardBGColor = Color.LightGray,
                    textColor = Color.Black,
                    buttonTexts = listOf("Start New Round", "Return to Main Menu"),
                    onButtonSelection = {
                        if (it == "Start New Round") {
                            snakeGameViewModel.startNewGame()
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
fun SnakeGameScreenPreview() {
    SnakeGameScreen(
        snakeGameViewModel = SnakeGameViewModel(),
        onNavigateToOtherScreen = {}
    )
}
