package com.mhd_07.courtly.core.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val darkScheme = darkColorScheme(
    primary = primary,
    onPrimary = onPrimary,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceContainer = surfaceContainerDark,
    error = errorDark,
    onError = onErrorDark,
)

val lightScheme = lightColorScheme(
    primary = primary,
    onPrimary = onPrimary,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceContainer = surfaceContainerLight,
    error = errorLight,
    onError = onErrorLight,
)

@Composable
fun CourtlyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) darkScheme else lightScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = CourtlyTypography(),
        shapes = courtlyShapes,
        content = content
    )
}