package com.example.android_games_app.games.twentyfortyeight.utils

import androidx.compose.ui.graphics.Color

object TileFunctions {
    fun getBackgroundColor(tileValue: Int): Color {
        return when (tileValue) {
            2 -> Color(0xFFECE4DB)
            4 -> Color(0xFFE8CCB5)
            8 -> Color(0xFFF2B179)
            16 -> Color(0xFFF59563)
            32 -> Color(0xFFF67C5F)
            64 -> Color(0xFFF65E3B)
            128 -> Color(0xFFEDCF72)
            256 -> Color(0xFFEDCC61)
            512 -> Color(0xFFEDC850)
            1024 -> Color(0xFFEDC53F)
            2048 -> Color(0xFFEDC22E)
            else -> Color.Gray
        }
    }
}