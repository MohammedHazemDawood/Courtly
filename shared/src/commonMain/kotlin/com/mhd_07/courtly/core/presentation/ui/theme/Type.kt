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
import courtly.shared.generated.resources.oswald
import org.jetbrains.compose.resources.Font

@Composable
fun CourtlyTypography(): Typography {
    val mainFont = FontFamily(Font(Res.font.oswald))

    return with(MaterialTheme.typography) {
        copy(
            // "Enter your email address"
            displayLarge = TextStyle(
                fontSize = 32.sp,
                fontFamily = mainFont,
                lineHeight = 36.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            ),

            displaySmall = displaySmall.copy(fontFamily = mainFont),
            displayMedium = displayMedium.copy(fontFamily = mainFont),

            // "We'll send you a verification code"
            bodyLarge = TextStyle(
                fontSize = 17.sp,
                fontFamily = mainFont,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Normal
            ),

            bodyMedium = bodyMedium.copy(fontFamily = mainFont),

            // Placeholder: "tom@example.com"
            headlineSmall = TextStyle(
                fontSize = 20.sp,
                fontFamily = mainFont,
                lineHeight = 28.sp,
                fontWeight = FontWeight.SemiBold,
                fontStyle = FontStyle.Normal,
                letterSpacing = 0.3.sp
            ),
            headlineMedium = headlineMedium.copy(fontFamily = mainFont),
            headlineLarge = headlineLarge.copy(fontFamily = mainFont),

            titleLarge = titleLarge.copy(fontFamily = mainFont),
            titleSmall = titleSmall.copy(fontFamily = mainFont),

            // "Continue"
            titleMedium = TextStyle(
                fontSize = 18.sp,
                fontFamily = mainFont,
                lineHeight = 22.sp,
                fontWeight = FontWeight.SemiBold
            ),

            // Terms & Privacy text
            bodySmall = TextStyle(
                fontSize = 14.sp,
                fontFamily = mainFont,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Normal
            ),

            // Top app bar action ("Use phone")
            labelLarge = TextStyle(
                fontSize = 17.sp,
                fontFamily = mainFont,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Medium
            ),
            labelMedium = labelMedium.copy(fontFamily = mainFont),
            labelSmall = labelSmall.copy(fontFamily = mainFont)
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
