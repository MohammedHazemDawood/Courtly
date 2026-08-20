package com.mhd_07.courtly.feature_match.presentation.components

import android.os.Build
import android.text.format.DateFormat
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import kotlin.time.Instant
import kotlin.time.toJavaInstant
import androidx.compose.ui.platform.LocalLocale
import java.util.Date

@Composable
actual fun Instant.toLocaleFormat(): String  {
    val context = LocalContext.current
    val is24Hour = DateFormat.is24HourFormat(context)
    val skeleton = if (is24Hour) "Hm" else "hm"
    val pattern = DateFormat.getBestDateTimePattern(LocalLocale.current.platformLocale, skeleton)

    val formatter =
        DateTimeFormatter.ofPattern(pattern, LocalLocale.current.platformLocale)
            .withZone(ZoneId.systemDefault())
    return formatter.format(this.toJavaInstant())
}
