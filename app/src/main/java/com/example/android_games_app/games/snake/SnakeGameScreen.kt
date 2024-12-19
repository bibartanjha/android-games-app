package com.example.android_games_app.games.snake

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_games_app.games.snake.utils.SnakeDirection
import com.example.android_games_app.games.snake.utils.SnakeFixedValues.NUM_GRID_COLS
import com.example.android_games_app.games.snake.utils.SnakeFixedValues.NUM_GRID_ROWS
import com.example.android_games_app.navigation.Routes
import com.example.android_games_app.utils.BaseCard
import com.example.android_games_app.utils.GamePauseOrCompleteScreen
import com.example.android_games_app.utils.GameProgressStatus
import com.example.android_games_app.utils.TopBarWithBackIcon

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SnakeGameScreen(
    snakeGameViewModel: SnakeGameViewModel,
    onPostGameOptionSelected: (optionName: String) -> Unit,
    onBackClicked: () -> Unit,
) {
    val gameState by snakeGameViewModel.getSnakeGameState.collectAsState()

    Scaffold(
        topBar = {
            TopBarWithBackIcon(
                gameName = "Snake",
                onIconClicked = onBackClicked
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = Color(red = 215, green = 42, blue = 120, alpha = 255)
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
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "Score: ${gameState.currentScore}",
                        color = Color.Black,
                        fontWeight = FontWeight.W800,
                        modifier = Modifier
                            .border(2.dp, Color.Black, shape = RoundedCornerShape(4.dp))
                            .padding(horizontal = 30.dp, vertical = 10.dp),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = { snakeGameViewModel.pauseGame() }) {
                        Text("⏸")
                    }
                }

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
                    val verticalMarginAroundGameBoard = 10.toFloat()

                    for (row in 0 until NUM_GRID_ROWS) {
                        for (col in 0 until NUM_GRID_COLS) {
                            drawRect(
                                color = Color.Gray,
                                size = androidx.compose.ui.geometry.Size(dimensionGridCell, dimensionGridCell),
                                topLeft = androidx.compose.ui.geometry.Offset(
                                    horizontalMarginAroundGameBoard + (col * dimensionGridCell),
                                    verticalMarginAroundGameBoard + (row * dimensionGridCell)
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
                                verticalMarginAroundGameBoard + (snakeCoordinates[snakePointIndex].row * dimensionGridCell)
                            ),
                        )
                    }

                    drawRect(
                        color = Color(red = 222, green = 196, blue = 55, alpha = 255),
                        size = androidx.compose.ui.geometry.Size(dimensionGridCell, dimensionGridCell),
                        topLeft = androidx.compose.ui.geometry.Offset(
                            horizontalMarginAroundGameBoard + (gameState.snakeFoodCoordinates.col * dimensionGridCell),
                            verticalMarginAroundGameBoard + (gameState.snakeFoodCoordinates.row * dimensionGridCell)
                        ),
                    )

                    drawRect(
                        color = Color.White,
                        size = androidx.compose.ui.geometry.Size(totalWidthOfGameBoard, totalHeightOfGameBoard),
                        topLeft = androidx.compose.ui.geometry.Offset(horizontalMarginAroundGameBoard, verticalMarginAroundGameBoard),
                        style = Stroke(width = 4.dp.toPx())
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        val arrowCardWidth = (screenWidth/7)
                        val arrowCardTextSize = (arrowCardWidth/2)
                        BaseCard(
                            textValue = "↑",
                            cardWidth = arrowCardWidth.dp,
                            cardHeight = arrowCardWidth.dp,
                            textSize = arrowCardTextSize.sp,
                            yOffset = -(arrowCardWidth).dp,
                            onBaseCardClicked = {
                                snakeGameViewModel.updateSnakeDirection(SnakeDirection.UP)
                            }
                        )

                        BaseCard(
                            textValue = "↓",
                            cardWidth = arrowCardWidth.dp,
                            cardHeight = arrowCardWidth.dp,
                            textSize = arrowCardTextSize.sp,
                            yOffset = (arrowCardWidth).dp,
                            onBaseCardClicked = {
                                snakeGameViewModel.updateSnakeDirection(SnakeDirection.DOWN)
                            }
                        )

                        BaseCard(
                            textValue = "←",
                            cardWidth = arrowCardWidth.dp,
                            cardHeight = arrowCardWidth.dp,
                            textSize = arrowCardTextSize.sp,
                            xOffset = (-arrowCardWidth).dp,
                            onBaseCardClicked = {
                                snakeGameViewModel.updateSnakeDirection(SnakeDirection.LEFT)
                            }
                        )

                        BaseCard(
                            textValue = "→",
                            cardWidth = arrowCardWidth.dp,
                            cardHeight = arrowCardWidth.dp,
                            textSize = arrowCardTextSize.sp,
                            xOffset = (arrowCardWidth).dp,
                            onBaseCardClicked = {
                                snakeGameViewModel.updateSnakeDirection(SnakeDirection.RIGHT)
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
                            snakeGameViewModel.resumeGame()
                        } else if (it == "Restart") {
                            snakeGameViewModel.startNewGame()
                        }
                    }
                )
            } else if (gameState.gameProgressStatus in listOf(GameProgressStatus.LOST, GameProgressStatus.WON)) {
                GamePauseOrCompleteScreen(
                    text = "Game Over!",
                    subTexts = listOf("You scored ${gameState.currentScore} points"),
                    cardBGColor = Color.LightGray,
                    textColor = Color.Black,
                    buttonTexts = listOf("Start New Round", "Return to Main Menu"),
                    onButtonSelection = {
                        if (it == "Start New Round") {
                            snakeGameViewModel.startNewGame()
                        } else if (it == "Return to Main Menu") {
                            snakeGameViewModel.startNewGame()
                            onPostGameOptionSelected(Routes.HOME_SCREEN)
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
        onPostGameOptionSelected = {},
        onBackClicked = {},
    )
}
