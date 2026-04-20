@file:Suppress("MagicNumber")

package ru.practicum.android.diploma.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object AppColors {
    val BlackUniversal = Color(0xFF1A1B22)
    val WhiteUniversal = Color(0xFFFDFDFD)
    val LightGray = Color(0xFFE6E8EB)
    val Blue = Color(0xFF3772E7)
    val Gray = Color(0xFFAEAFB4)
    val Red = Color(0xFFF56B6C)
}

private val LightColorScheme = lightColorScheme(
    primary = AppColors.Blue,
    onPrimary = AppColors.WhiteUniversal,
    secondary = AppColors.Gray,
    onSecondary = AppColors.BlackUniversal,
    background = AppColors.BlackUniversal.copy(alpha = 0.5f),
    onBackground = AppColors.BlackUniversal,
    surface = AppColors.WhiteUniversal,
    onSurface = AppColors.BlackUniversal,
    error = AppColors.Red,
    onError = AppColors.WhiteUniversal,
    surfaceVariant = AppColors.LightGray,
    onSurfaceVariant = AppColors.BlackUniversal
)

private val DarkColorScheme = darkColorScheme(
    primary = AppColors.Blue,
    onPrimary = AppColors.BlackUniversal,
    secondary = AppColors.Gray,
    onSecondary = AppColors.WhiteUniversal,
    background = AppColors.BlackUniversal.copy(alpha = 0.5f),
    onBackground = AppColors.WhiteUniversal,
    surface = AppColors.BlackUniversal,
    onSurface = AppColors.WhiteUniversal,
    error = AppColors.Red,
    onError = AppColors.BlackUniversal,
    surfaceVariant = AppColors.LightGray,
    onSurfaceVariant = AppColors.BlackUniversal
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

