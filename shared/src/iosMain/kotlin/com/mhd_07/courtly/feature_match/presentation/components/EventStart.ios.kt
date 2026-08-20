package com.mhd_07.courtly.feature_match.presentation.components

import androidx.compose.runtime.Composable
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterShortStyle
import platform.Foundation.dateWithTimeIntervalSince1970
import kotlin.time.Instant

@Composable
actual fun Instant.toLocaleFormat(): String {
    val date = NSDate.dateWithTimeIntervalSince1970(epochSeconds.toDouble())
    val formatter = NSDateFormatter().apply {
        timeStyle = NSDateFormatterShortStyle
    }
    return formatter.stringFromDate(date)
}