package com.example.composeactivity.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val QuoteBackground = Color(0xFFF5F5F5)
val LikedColor = Color.Red
val SavedColor = Color.Blue
val IconDefaultColor = Color.Black

val CategorySelected = Color(0xFFFFC300)
val CategoryUnselected = Color(0xFFEAEAEA).copy(alpha = 0.7f)
val CategoryText = Color.Black

val ColorScheme.quoteBackground: Color
    get() = QuoteBackground

object AppBarColors {
    val Background = Color(0xFF16213E)
    val TitleText = Color.White
    val SearchText = Color.White
    val SearchPlaceholder = Color.White.copy(alpha = 0.6f)
    val SearchUnfocusedIndicator = Color.White.copy(alpha = 0.3f)
    val SearchFocusedIndicator = Color.White
    val IconTint = Color.White
}