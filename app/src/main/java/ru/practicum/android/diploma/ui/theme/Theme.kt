package ru.practicum.android.diploma.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Blue,
    onPrimary = WhiteUniversal,
    secondary = Gray,
    onSecondary = BlackUniversal,
    background = BlackUniversal.copy(alpha = 0.5f),
    onBackground = BlackUniversal,
    surface = WhiteUniversal,
    onSurface = BlackUniversal,
    error = Red,
    onError = WhiteUniversal,
    surfaceVariant = LightGray,
    onSurfaceVariant = BlackUniversal
)

private val DarkColorScheme = darkColorScheme(
    primary = Blue,
    onPrimary = BlackUniversal,
    secondary = Gray,
    onSecondary = WhiteUniversal,
    background = BlackUniversal.copy(alpha = 0.5f),
    onBackground = WhiteUniversal,
    surface = BlackUniversal,
    onSurface = WhiteUniversal,
    error = Red,
    onError = BlackUniversal,
    surfaceVariant = LightGray,
    onSurfaceVariant = BlackUniversal
)

private val typography = MaterialTypography

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        content = content
    )
}
