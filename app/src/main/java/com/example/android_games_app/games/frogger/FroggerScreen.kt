package com.example.android_games_app.games.frogger

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android_games_app.R
import com.example.android_games_app.games.frogger.utils.Frog
import com.example.android_games_app.games.frogger.FroggerFixedValues.FROGGER_SCREEN_BG_COLOR
import com.example.android_games_app.games.frogger.FroggerFixedValues.columnWidth
import com.example.android_games_app.games.frogger.FroggerFixedValues.gameRows
import com.example.android_games_app.games.frogger.FroggerFixedValues.rowHeight
import com.example.android_games_app.games.frogger.utils.GameRowType
import com.example.android_games_app.games.frogger.utils.RowObject
import com.example.android_games_app.navigation.Routes
import com.example.android_games_app.utils.DirectionButtons
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
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((screenHeight * 0.7).dp)
                        .paint(
                            painterResource(id = R.drawable.frogger_background),
                            contentScale = ContentScale.FillBounds
                        )
                ) {
                    if (gameState.objectXOffsets.isNotEmpty()) {
                        for (rowNumber in 0 until gameState.objectXOffsets.size) {
                            val rowInformation = gameRows[rowNumber]
                            if (rowInformation.rowType == GameRowType.ROAD) {
                                for (obstacleXOffset in gameState.objectXOffsets[rowNumber]) {
                                    val obstacleImageResourceValue = RowObject.objectToImageMap[rowInformation.objectTypeInLane]
                                    val obstacleImagePainter = if (obstacleImageResourceValue == null) {
                                        painterResource(id = RowObject.getDefaultObjectImage())
                                    } else {
                                        painterResource(id = obstacleImageResourceValue)
                                    }

//                                    val yOffset = maxYOffset - ((gameState.objectXOffsets.size - rowNumber) * rowHeight)

                                    val obstacleWidth = if (rowInformation.containsWiderObject) {
                                        columnWidth * 2
                                    } else {
                                        columnWidth
                                    }
                                    Image(
                                        modifier = Modifier
                                            .width(obstacleWidth.dp)
                                            .height(rowHeight.dp)
                                            .padding(start = 0.dp)
                                            .offset(
                                                x = obstacleXOffset.dp,
                                                y = rowInformation.yOffsetValueOnScreen.dp
                                            )
                                        ,
                                        painter = obstacleImagePainter,
                                        contentDescription = null,
                                    )
                                }
                            }
                        }
                    }

                    val frogImageResourceValue = Frog.directionToImageMap[gameState.frogDirection]
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
                                y = gameState.frogYOffset.dp
                            )
                        ,
                        painter = frogImagePainter,
                        contentDescription = null,
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))

                DirectionButtons(
                    arrowCardWidth = (screenWidth/7).toInt(),
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