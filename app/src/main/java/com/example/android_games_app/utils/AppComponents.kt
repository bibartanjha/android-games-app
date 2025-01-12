package com.example.android_games_app.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_games_app.R

@Composable
fun BaseCard(
    textValue: String,
    cardWidth: Dp = 100.dp,
    cardHeight: Dp = 100.dp,
    backgroundColor: Color = Color.Unspecified,
    textColor: Color = Color.Black,
    textSize: TextUnit = 24.sp,
    onBaseCardClicked: (() -> Unit)? = null,
    xOffset: Dp = 0.dp,
    yOffset: Dp = 0.dp
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(24.dp)
            .width(cardWidth)
            .height(cardHeight)
            .offset(xOffset, yOffset),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(enabled = onBaseCardClicked != null) {
                    onBaseCardClicked?.invoke()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = textValue,
                fontSize = textSize,
                color = textColor,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
fun BaseCardPreview() {
    BaseCard(
        textValue = "Base Card"
    )
}

@Composable
fun ImageCard(
    cardWidth: Dp = 100.dp,
    cardHeight: Dp = 100.dp,
    painter: Painter,
    backgroundColor: Color = Color.Unspecified,
    onBaseCardClicked: (() -> Unit)? = null,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(24.dp)
            .width(cardWidth)
            .height(cardHeight),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(enabled = onBaseCardClicked != null) {
                    onBaseCardClicked?.invoke()
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painter,
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun ImageCardPreview() {
    ImageCard(
        painter = painterResource(R.drawable.snake)
    )
}

@Composable
fun OverlayMenuScreen(
    text: String,
    subTexts: List<String> = emptyList(),
    cardBGColor: Color,
    textColor: Color,
    buttonTexts: List<String>,
    onButtonSelection: (buttonText: String) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    
    OverlayScreen(
        screenColor = Color.Black,
        screenWidth = screenWidth.dp,
        screenHeight = screenHeight.dp
    )

    Menu(text, subTexts, cardBGColor, textColor, buttonTexts, onButtonSelection)
}

@Preview
@Composable
fun OverlayMenuScreenPreview() {
    OverlayMenuScreen(
        text = "Congrats on finishing the game!",
        subTexts = listOf("You scored 33 points", "Pick a next option"),
        cardBGColor = Color.Cyan,
        textColor = Color.Black,
        buttonTexts = listOf("Restart", "Resume"),
        onButtonSelection = {}
    )
}

@Composable
fun OverlayScreen(
    screenColor: Color,
    screenShape: Shape = RectangleShape,
    screenWidth: Dp,
    screenHeight: Dp,
    text: String? = null,
) {
    Box(
        modifier = Modifier
            .width(screenWidth)
            .height(screenHeight)
            .background(
                color = screenColor.copy(
                    alpha = 0.5f
                ),
                shape = screenShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (text != null) {
            Text(
                text = text,
                color = Color.Black,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview
@Composable
fun OverlayScreenPreview() {
    OverlayScreen(
        screenColor = Color.Magenta,
        screenShape = RectangleShape,
        screenWidth = 100.dp,
        screenHeight = 100.dp,
        text = "Hello"
    )
}

@Composable
fun Menu(
    text: String,
    subTexts: List<String> = emptyList(),
    cardBGColor: Color,
    textColor: Color,
    buttonTexts: List<String>,
    onButtonSelection: (buttonText: String) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .padding(24.dp),
            colors = CardDefaults.cardColors(containerColor = cardBGColor),
            elevation = CardDefaults.cardElevation(4.dp),

            ) {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = text,
                    fontSize = (screenWidth / 17).sp,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                for (subText in subTexts) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = subText,
                        fontSize = (screenWidth / 25).sp,
                        color = textColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                for (buttonText in buttonTexts) {
                    Button(onClick = { onButtonSelection(buttonText) }) {
                        Text(buttonText)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MenuPreview() {
    Menu(
        text = "Menu",
        cardBGColor = Color.Cyan,
        textColor = Color.Black,
        buttonTexts = listOf("Button1", "Button2"),
        onButtonSelection = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithBackIcon(
    gameName: String,
    onIconClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = gameName)
        },
        navigationIcon = {
            IconButton(
                onClick = { onIconClicked() }
            ) {
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

@Preview
@Composable
fun TopBarWithBackIconPreview() {
    TopBarWithBackIcon(
        gameName = "Monopoly",
        onIconClicked = {}
    )
}

@Composable
fun DirectionButtons(
    arrowCardWidth: Int,
    onUpClicked: (() -> Unit)? = null,
    onDownClicked: (() -> Unit)? = null,
    onLeftClicked: (() -> Unit)? = null,
    onRightClicked: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val arrowCardTextSize = (arrowCardWidth/2)
        BaseCard(
            textValue = "↑",
            cardWidth = arrowCardWidth.dp,
            cardHeight = arrowCardWidth.dp,
            textSize = arrowCardTextSize.sp,
            yOffset = -(arrowCardWidth).dp,
            onBaseCardClicked = onUpClicked
        )

        BaseCard(
            textValue = "↓",
            cardWidth = arrowCardWidth.dp,
            cardHeight = arrowCardWidth.dp,
            textSize = arrowCardTextSize.sp,
            yOffset = (arrowCardWidth).dp,
            onBaseCardClicked = onDownClicked
        )

        BaseCard(
            textValue = "←",
            cardWidth = arrowCardWidth.dp,
            cardHeight = arrowCardWidth.dp,
            textSize = arrowCardTextSize.sp,
            xOffset = (-arrowCardWidth).dp,
            onBaseCardClicked = onLeftClicked
        )

        BaseCard(
            textValue = "→",
            cardWidth = arrowCardWidth.dp,
            cardHeight = arrowCardWidth.dp,
            textSize = arrowCardTextSize.sp,
            xOffset = (arrowCardWidth).dp,
            onBaseCardClicked = onRightClicked
        )
    }
}

@Preview
@Composable
fun DirectionButtonsPreview() {
    DirectionButtons(
        arrowCardWidth = 20
    )
}

