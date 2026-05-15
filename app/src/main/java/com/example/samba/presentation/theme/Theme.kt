package com.example.samba.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = SambaPrimary,
    onPrimary = SambaTextPrimary,
    secondary = SambaAccent,
    onSecondary = SambaBackground,
    background = SambaBackground,
    onBackground = SambaTextPrimary,
    surface = SambaSurface,
    onSurface = SambaTextPrimary,
    surfaceVariant = SambaSurfaceVariant,
    onSurfaceVariant = SambaTextSecondary,
    error = SambaError,
    onError = SambaTextPrimary
)

@Composable
fun SambaAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = SambaTypography,
        content = content
    )
}
