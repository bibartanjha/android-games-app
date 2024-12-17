package com.example.android_games_app.games.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
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
import com.example.android_games_app.games.ui.screens.Routes

@Composable
fun BaseCard(
    textValue: String,
    cardWidth: Dp,
    cardHeight: Dp,
    backgroundColor: Color = Color.Unspecified,
    textColor: Color = Color.Black,
    textSize: TextUnit = 24.sp,
    onBaseCardClicked: (() -> Unit)? = null
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(24.dp)
            .width(cardWidth)
            .height(cardHeight)
        ,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .let {
                    if (onBaseCardClicked != null)  {
                        it.clickable {
                            onBaseCardClicked()
                        }
                    } else {
                        it
                    }
                 }
            ,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = textValue,
                fontSize = textSize,
                color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun TextViewWithMessages(
    text: String,
    subTexts: List<String>,
    backgroundColor: Color,
    textColor: Color
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(24.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(
                    text = text,
                    fontSize = (screenWidth/17).sp,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                for (subText in subTexts) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = subText,
                        fontSize = (screenWidth/25).sp,
                        color = textColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun TextViewWithMessagesPreview() {
    TextViewWithMessages(
        text = "Congrats on finishing the game!",
        subTexts = listOf("You scored 33 points", "Pick a next option"),
        backgroundColor = Color.Cyan,
        textColor = Color.Black
    )
}


@Composable
fun PostGameOptions(
    onPostGameOptionSelected: (optionName: String) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    BaseCard(
        textValue = "Start New Round",
        cardWidth = (screenWidth/1.37).dp,
        cardHeight = (screenHeight/16.8).dp,
        textSize = (screenWidth/17.125).sp,
        onBaseCardClicked = {
            onPostGameOptionSelected(Routes.WORDLE_SCREEN)
        }
    )
    BaseCard(
        textValue = "Return to Main Menu",
        cardWidth = (screenWidth/1.37).dp,
        cardHeight = (screenHeight/16.8).dp,
        textSize = (screenWidth/17.125).sp,
        onBaseCardClicked = {
            onPostGameOptionSelected(Routes.HOME_SCREEN)
        }
    )
}


