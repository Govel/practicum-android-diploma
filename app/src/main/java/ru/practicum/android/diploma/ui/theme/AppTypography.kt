package ru.practicum.android.diploma.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object AppTypography {
    val bold32 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 38.sp,
        letterSpacing = 0.sp
    )

    val medium22 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp
    )

    val medium16 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 19.sp,
        letterSpacing = 0.sp
    )

    val regular16 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 19.sp,
        letterSpacing = 0.sp
    )

    val regular12 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    )

}

val MaterialTypography = Typography(
    displayLarge = AppTypography.bold32,
    titleLarge = AppTypography.medium22,
    bodyMedium = AppTypography.medium16,
    bodyLarge = AppTypography.regular16,
    bodySmall = AppTypography.regular12
)

