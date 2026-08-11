package com.mhd_07.courtly.core.presentation.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val LocalDimensions = compositionLocalOf { Dimensions() }

data class Dimensions(
    val default: Dp = 0.dp,
    val xxSmall: Dp = 2.dp,
    val xSmall: Dp = 8.dp,
    val small: Dp = 16.dp,
    val medium: Dp = 24.dp,
    val large: Dp = 32.dp,
    val xLarge: Dp = 40.dp,
    val xxLarge: Dp = 48.dp,
    val xxxLarge: Dp = 56.dp,
    val xxxxLarge: Dp = 64.dp,
)