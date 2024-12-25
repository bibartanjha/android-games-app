package com.example.android_games_app.games.twentyfortyeight

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_games_app.games.twentyfortyeight.utils.FixedValues.SCREEN_BG_COLOR
import com.example.android_games_app.games.twentyfortyeight.utils.GridTile
import com.example.android_games_app.games.twentyfortyeight.utils.SwipeDirection
import com.example.android_games_app.games.twentyfortyeight.utils.TileFunctions
import com.example.android_games_app.navigation.Routes
import com.example.android_games_app.utils.TopBarWithBackIcon
import kotlinx.coroutines.delay
import kotlin.math.abs

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TwentyFortyEightGameScreen(
    twentyFortyEightGameViewModel: TwentyFortyEightGameViewModel,
    onNavigateToOtherScreen: (optionName: String) -> Unit
) {
    val gameState by twentyFortyEightGameViewModel.getGameState.collectAsState()

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
            val configuration = LocalConfiguration.current
            val screenWidth = configuration.screenWidthDp
            val dimensionGridCell = (screenWidth/4.5).toFloat()

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                var totalDragX = 0f
                var totalDragY = 0f

                Box(
                    Modifier
                        .padding(8.dp)
                        .background(
                            color = Color.DarkGray,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDrag = { change, dragAmount ->
                                    totalDragX += dragAmount.x
                                    totalDragY += dragAmount.y
                                    change.consume()
                                },
                                onDragEnd = {
//                                    Log.d("2048 Log","--- $totalDragX")
//                                    Log.d("2048 Log","--- $totalDragY")
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
//                                    Log.d("2048 Log","--- $swipeDirection")
                                    totalDragX = 0F
                                    totalDragY = 0F
                                    twentyFortyEightGameViewModel.userMove(swipeDirection)
                                }
                            )
                        }
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.height(4.dp))
                        for (row in gameState.gameGrid.indices) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                            ) {
                                for (col in gameState.gameGrid[row].indices) {
                                    val tile = gameState.gameGrid[row][col]

                                    val animatedScaleFactor = remember { Animatable(if (tile.isNewTile) 0.7f else 1f) }

                                    LaunchedEffect(tile) {
                                        if (tile.hadRecentMerge) {
                                            animatedScaleFactor.animateTo(
                                                targetValue = 1.1f,
                                                animationSpec = tween(durationMillis = 150)
                                            )
                                            animatedScaleFactor.animateTo(
                                                targetValue = 1f,
                                                animationSpec = tween(durationMillis = 150)
                                            )

                                            twentyFortyEightGameViewModel.resetHadRecentMerge(row, col)
                                        }
                                        else if (tile.isNewTile) {
                                            animatedScaleFactor.snapTo(0.7f)
                                            animatedScaleFactor.animateTo(
                                                targetValue = 1f,
                                                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                                            )
                                            twentyFortyEightGameViewModel.resetIsNewTile(row, col)
                                        }
                                    }

                                    Box(
                                        modifier = Modifier
                                            .size(dimensionGridCell.dp)
                                            .background(
                                                color = TileFunctions.getBackgroundColor(0),
                                                shape = RoundedCornerShape(6.dp)
                                            )
                                        ,
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .graphicsLayer(
                                                    scaleX = animatedScaleFactor.value,
                                                    scaleY = animatedScaleFactor.value
                                                )
                                                .fillMaxSize()
                                                .background(
                                                    color = TileFunctions.getBackgroundColor(tile.value),
                                                    shape = RoundedCornerShape(6.dp)
                                                )
                                            ,
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (tile.value != 0) {
                                                val numDigits = tile.value.toString().length
                                                val fontSize = when (numDigits) {
                                                    1 -> (dimensionGridCell / 4).sp
                                                    2 -> (dimensionGridCell / 4.5).sp
                                                    3 -> (dimensionGridCell / 5).sp
                                                    else -> (dimensionGridCell / 6).sp
                                                }

                                                Text(
                                                    text = tile.value.toString(),
                                                    color = Color.Black,
                                                    fontSize = fontSize * animatedScaleFactor.value,
                                                    textAlign = TextAlign.Center,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
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