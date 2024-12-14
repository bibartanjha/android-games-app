package com.example.android_games_app.games.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HeaderCard(
    headerText: String,
    backgroundColor: Color,
    textColor: Color
) {
    BaseCard(
        textValue = headerText,
        cardWidth = 300.dp,
        cardHeight = 50.dp,
        backgroundColor = backgroundColor,
        textColor = textColor,
        textSize = 24.sp
    )
}

@Preview
@Composable
fun HeaderCardPreview() {
    HeaderCard(
        headerText = "Sample Header",
        backgroundColor = Color.Blue,
        textColor = Color.Red
    )
}

@Composable
fun OptionCard(
    optionName: String,
    onOptionCardClicked: (optionName: String) -> Unit
) {
    BaseCard(
        textValue = optionName,
        cardWidth = 130.dp,
        cardHeight = 130.dp,
        textColor = Color.Black,
        textSize = 18.sp,
        onBasedCardClicked = { onOptionCardClicked(optionName) }
    )
}

@Preview
@Composable
fun OptionCardPreview() {
    OptionCard(
        optionName = "Sample Option",
        onOptionCardClicked = {}
    )
}

@Composable
fun BaseCard(
    textValue: String,
    cardWidth: Dp,
    cardHeight: Dp,
    backgroundColor: Color = Color.Unspecified,
    textColor: Color = Color.Black,
    textSize: TextUnit = 24.sp,
    onBasedCardClicked: (() -> Unit)? = null
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
                .let {
                    if (onBasedCardClicked != null)  {
                        it.clickable {
                            onBasedCardClicked()
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
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

    }
}
