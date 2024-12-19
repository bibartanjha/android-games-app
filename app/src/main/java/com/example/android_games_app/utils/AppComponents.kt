package com.example.android_games_app.utils

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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
fun GamePauseOrCompleteScreen(
    text: String,
    subTexts: List<String> = emptyList(),
    cardBGColor: Color,
    textColor: Color,
    buttonTexts: List<String>,
    onButtonSelection: (buttonText: String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color.Black.copy(
                    alpha = 0.5f
                )
            )
    )

    Menu(text, subTexts, cardBGColor, textColor, buttonTexts, onButtonSelection)
}

@Preview
@Composable
fun GamePauseOrCompleteScreenPreview() {
    GamePauseOrCompleteScreen(
        text = "Congrats on finishing the game!",
        subTexts = listOf("You scored 33 points", "Pick a next option"),
        cardBGColor = Color.Cyan,
        textColor = Color.Black,
        buttonTexts = listOf("Restart", "Resume"),
        onButtonSelection = {}
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


