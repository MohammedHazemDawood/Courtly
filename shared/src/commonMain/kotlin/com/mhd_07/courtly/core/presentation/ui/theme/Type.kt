package com.mhd_07.courtly.core.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.righteous
import org.jetbrains.compose.resources.Font

@Composable
fun CourtlyTypography(): Typography {
    val orbitron = FontFamily(Font(Res.font.righteous))

    return with(MaterialTheme.typography) {
        copy(
            displayLarge = displayLarge.copy(
                fontFamily = orbitron,
                fontWeight = FontWeight.Bold
            ),
            displayMedium = displayMedium.copy(
                fontFamily = orbitron,
                fontWeight = FontWeight.Bold
            ),
            displaySmall = displaySmall.copy(
                fontFamily = orbitron,
                fontWeight = FontWeight.Bold
            ),
            headlineLarge = headlineLarge.copy(
                fontFamily = orbitron,
                fontWeight = FontWeight.Bold
            ),
            headlineMedium = headlineMedium.copy(
                fontFamily = orbitron,
                fontWeight = FontWeight.Bold
            ),
            headlineSmall = headlineSmall.copy(
                fontFamily = orbitron,
                fontWeight = FontWeight.Bold
            ),
            titleLarge = titleLarge.copy(
                fontFamily = orbitron,
                fontWeight = FontWeight.Bold
            ),
            titleMedium = titleMedium.copy(
                fontFamily = orbitron,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            ),
            titleSmall = titleSmall.copy(
                fontFamily = orbitron,
                fontWeight = FontWeight.Bold
            ),
            labelLarge = labelLarge.copy(
                fontFamily = orbitron,
                fontWeight = FontWeight.Normal
            ),
            labelMedium = labelMedium.copy(
                fontFamily = orbitron,
                fontWeight = FontWeight.Normal
            ),
            labelSmall = labelSmall.copy(
                fontFamily = orbitron,
                fontWeight = FontWeight.Normal
            ),
            bodyLarge = bodyLarge.copy(
                fontFamily = orbitron,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            ),
            bodyMedium = bodyMedium.copy(
                fontFamily = orbitron,
                fontWeight = FontWeight.Normal
            ),
            bodySmall = bodySmall.copy(
                fontFamily = orbitron,
                fontWeight = FontWeight.Normal
            ),
        )
    }

}