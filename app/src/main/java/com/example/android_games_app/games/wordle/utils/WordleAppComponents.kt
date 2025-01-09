package com.example.android_games_app.games.wordle.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_games_app.games.wordle.utils.WordleFixedValues.BUTTON_COLOR


@Composable
fun LetterGuessTextView(
    backgroundColor: Color = Color(red = 233, green = 218, blue = 193),
    letterInView: String = "",
    boxSize: Int = 74
) {
    Box(
        modifier = Modifier
            .size((boxSize).dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(6.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letterInView,
            fontSize = (boxSize/2).sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Clip
        )
    }
}

@Preview
@Composable
fun LetterGuessTextViewPreview() {
    LetterGuessTextView(letterInView = "B")
}

@Composable
fun TextInputButton(
    backgroundColor: Color = BUTTON_COLOR,
    buttonClicked: (buttonText: String) -> Unit,
    textInButton: String = "A",
    buttonWidth: Dp = 32.dp,
    buttonHeight: Dp = 64.dp,
    fontSize: TextUnit = 26.sp
) {
    Box(
        modifier = Modifier
            .width(buttonWidth)
            .height(buttonHeight)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable {
                buttonClicked(textInButton)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = textInButton,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Clip
        )
    }
}

@Preview
@Composable
fun TextInputButtonPreview() {
    TextInputButton(textInButton = "A", buttonClicked = {})
}

