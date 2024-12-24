package com.example.android_games_app.games.twentyfortyeight

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_games_app.games.twentyfortyeight.utils.FixedValues.GRID_COLS
import com.example.android_games_app.games.twentyfortyeight.utils.FixedValues.GRID_ROWS
import com.example.android_games_app.games.twentyfortyeight.utils.FixedValues.SCREEN_BG_COLOR
import com.example.android_games_app.games.twentyfortyeight.utils.SwipeDirection
import com.example.android_games_app.games.twentyfortyeight.utils.TileFunctions
import com.example.android_games_app.navigation.Routes
import com.example.android_games_app.utils.TopBarWithBackIcon
import kotlin.math.abs

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TwentyFortyEightGameScreen(
    twentyFortyEightGameViewModel: TwentyFortyEightGameViewModel,
    onNavigateToOtherScreen: (optionName: String) -> Unit
) {
    val gameState by twentyFortyEightGameViewModel.getGameState.collectAsState()

    Log.d("2048 Debug", "GameState Recomposition: $gameState")

    Scaffold(
        topBar = {
            TopBarWithBackIcon(
                gameName = "2048",
                onIconClicked = {
                    onNavigateToOtherScreen(Routes.HOME_SCREEN)
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = SCREEN_BG_COLOR
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val configuration = LocalConfiguration.current
                val screenWidth = configuration.screenWidthDp
                val screenHeight = configuration.screenHeightDp

                var totalDragX = 0f
                var totalDragY = 0f

                val textMeasurer = rememberTextMeasurer()

                Canvas(
                    modifier = Modifier
                        .width(screenWidth.dp)
                        .height((screenHeight*.6).dp)
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDrag = { change, dragAmount ->
                                    totalDragX += dragAmount.x
                                    totalDragY += dragAmount.y
                                    change.consume()
                                },
                                onDragEnd = {
                                    Log.d("2048 Log","--- $totalDragX")
                                    Log.d("2048 Log","--- $totalDragY")
                                    val swipeDirection =
                                        if (abs(totalDragX) > abs(totalDragY)) {
                                            if (totalDragX > 0) {
                                                SwipeDirection.RIGHT
                                            } else {
                                                SwipeDirection.LEFT
                                            }
                                        } else {
                                            if (totalDragY > 0) {
                                                SwipeDirection.DOWN
                                            } else {
                                                SwipeDirection.UP
                                            }
                                        }
                                    Log.d("2048 Log","--- $swipeDirection")
                                    totalDragX = 0F
                                    totalDragY = 0F
                                    twentyFortyEightGameViewModel.userMove(swipeDirection)
                                }
                            )
                        }
                ) {
                    val canvasWidth = size.width

                    val dimensionGridCell = (canvasWidth/4.5).toFloat()

                    val totalWidthOfGameBoard = dimensionGridCell * GRID_COLS
                    val totalHeightOfGameBoard = dimensionGridCell * GRID_ROWS

                    val horizontalMarginAroundGameBoard = (canvasWidth - totalWidthOfGameBoard) / 2
                    val verticalMarginAroundGameBoard = 10.toFloat()

                    gameState.gameGrid.flatten().forEach { tile ->
                        val topLeftOfRect = Offset(
                            horizontalMarginAroundGameBoard + (tile.position.second * dimensionGridCell),
                            verticalMarginAroundGameBoard + (tile.position.first * dimensionGridCell)
                        )

                        val rectSize = Size(dimensionGridCell, dimensionGridCell)


                        drawRoundRect(
                            color = TileFunctions.getBackgroundColor(tile.value),
                            size = rectSize,
                            topLeft = topLeftOfRect,
                            cornerRadius = CornerRadius(8.dp.toPx())
                        )

                        if (tile.hadRecentMerge) {
                            drawRoundRect(
                                color = Color.Magenta,
                                size = rectSize,
                                topLeft = topLeftOfRect,
                                cornerRadius = CornerRadius(8.dp.toPx()),
                                style = Stroke(width = 4.dp.toPx())
                            )
                        }

                        if (tile.value != 0) {
                            val numDigits = tile.value.toString().length
                            val fontSize = when (numDigits) {
                                1 -> (dimensionGridCell/4).sp
                                2 -> (dimensionGridCell/4.5).sp
                                3 -> (dimensionGridCell/5).sp
                                else -> (dimensionGridCell/6).sp
                            }

                            drawText(
                                textMeasurer = textMeasurer,
                                text = tile.value.toString(),
                                style = TextStyle(
                                    fontSize = fontSize,
                                    color = Color.Black,
                                    textAlign = TextAlign.Center
                                ),
                                topLeft = topLeftOfRect
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TwentyFortyEightGameScreenPreview() {
    TwentyFortyEightGameScreen(
        twentyFortyEightGameViewModel = TwentyFortyEightGameViewModel(),
        onNavigateToOtherScreen = {}
    )
}