package com.example.uas_mocom.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val DarkBackground = Color(0xFF0F172A)
val CardSurface = Color(0xFF1E293B)
val PrimaryBlue = Color(0xFF3B82F6)
val TextWhite = Color(0xFFFFFFFF)
val TextGray = Color(0xFF94A3B8)
val AccentRed = Color(0xFFEF4444)
val AccentYellow = Color(0xFFEAB308)
val SuccessGreen = Color(0xFF22C55E)

@Composable
fun ScoreboardTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            background = DarkBackground, surface = CardSurface, primary = PrimaryBlue,
            onBackground = TextWhite, onSurface = TextWhite
        ),
        content = content
    )
}