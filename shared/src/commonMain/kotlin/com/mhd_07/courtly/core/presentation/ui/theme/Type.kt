package com.mhd_07.courtly.core.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.alexandria
import courtly.shared.generated.resources.montserrat
import org.jetbrains.compose.resources.Font

@Composable
fun CourtlyTypography(): Typography {
    val mainFont = FontFamily(
//        Font(Res.font.montserrat),
        Font(Res.font.alexandria),
    )


    return with(MaterialTheme.typography) {
        copy(
            displayLarge = TextStyle(
                fontSize = 32.sp,
                fontFamily = mainFont,
                lineHeight = 36.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            ),
            displayMedium = displayMedium.copy(fontFamily = mainFont),
            displaySmall = displaySmall.copy(fontFamily = mainFont),
            headlineLarge = headlineLarge.copy(fontFamily = mainFont),
            headlineMedium = headlineMedium.copy(fontFamily = mainFont),
            headlineSmall = TextStyle(
                fontSize = 20.sp,
                fontFamily = mainFont,
                lineHeight = 28.sp,
                fontWeight = FontWeight.SemiBold,
                fontStyle = FontStyle.Normal,
                letterSpacing = 0.3.sp
            ),
            titleLarge = titleLarge.copy(fontFamily = mainFont),
            titleMedium = TextStyle(
                fontSize = 18.sp,
                fontFamily = mainFont,
                lineHeight = 22.sp,
                fontWeight = FontWeight.SemiBold
            ),
            titleSmall = titleSmall.copy(fontFamily = mainFont),
            bodyLarge = bodyLarge.copy(fontFamily = mainFont),
            bodyMedium = bodyMedium.copy(fontFamily = mainFont),
            bodySmall = TextStyle(
                fontSize = 14.sp,
                fontFamily = mainFont,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Normal
            ),
            labelLarge = TextStyle(
                fontSize = 17.sp,
                fontFamily = mainFont,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Medium
            ),
            labelMedium = labelMedium.copy(fontFamily = mainFont),
            labelSmall = labelSmall.copy(fontFamily = mainFont),
        )
    }
}

val normalTextStyle
    @Composable get() = MaterialTheme.typography.bodyLarge
val titleTextStyle
    @Composable get() = MaterialTheme.typography.displayLarge
val fieldsTextStyle
    @Composable get() = MaterialTheme.typography.headlineSmall
val buttonTextStyle
    @Composable get() = MaterialTheme.typography.titleMedium
val notesTextStyle
    @Composable get() = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
