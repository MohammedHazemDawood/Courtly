package com.mhd_07.courtly.core.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.oswald
import org.jetbrains.compose.resources.Font

@Composable
fun CourtlyTypography(): Typography {
    val mainFont = FontFamily(Font(Res.font.oswald))

    return with(MaterialTheme.typography) {
        copy(
            displayLarge = displayLarge.copy(
                fontFamily = mainFont,
                fontWeight = FontWeight.Bold
            ),
            displayMedium = displayMedium.copy(
                fontFamily = mainFont,
                fontWeight = FontWeight.Bold
            ),
            displaySmall = displaySmall.copy(
                fontFamily = mainFont,
                fontWeight = FontWeight.Bold
            ),
            headlineLarge = headlineLarge.copy(
                fontFamily = mainFont,
                fontWeight = FontWeight.Bold
            ),
            headlineMedium = headlineMedium.copy(
                fontFamily = mainFont,
                fontWeight = FontWeight.Bold
            ),
            headlineSmall = headlineSmall.copy(
                fontFamily = mainFont,
                fontWeight = FontWeight.Bold
            ),
            titleLarge = titleLarge.copy(
                fontFamily = mainFont,
                fontWeight = FontWeight.Bold
            ),
            titleMedium = titleMedium.copy(
                fontFamily = mainFont,
                fontWeight = FontWeight.Bold,
            ),
            titleSmall = titleSmall.copy(
                fontFamily = mainFont,
                fontWeight = FontWeight.Bold
            ),
            labelLarge = labelLarge.copy(
                fontFamily = mainFont,
                fontWeight = FontWeight.Normal
            ),
            labelMedium = labelMedium.copy(
                fontFamily = mainFont,
                fontWeight = FontWeight.Normal
            ),
            labelSmall = labelSmall.copy(
                fontFamily = mainFont,
                fontWeight = FontWeight.Normal
            ),
            bodyLarge = bodyLarge.copy(
                fontFamily = mainFont,
                fontWeight = FontWeight.Normal,
            ),
            bodyMedium = bodyMedium.copy(
                fontFamily = mainFont,
                fontWeight = FontWeight.Normal
            ),
            bodySmall = bodySmall.copy(
                fontFamily = mainFont,
                fontWeight = FontWeight.Normal
            ),
        )
    }

}