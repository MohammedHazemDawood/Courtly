package com.mhd_07.courtly

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import com.mhd_07.courtly.core.presentation.ui.theme.CourtlyTheme
import com.mhd_07.courtly.core.presentation.ui.theme.Dimensions
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.feature_match_record.presentation.screen.MatchScreen
import com.mhd_07.courtly.feature_match_record.presentation.screen.MatchSetupScreen
import com.mhd_07.courtly.nav.AppNavigator

@Composable
@Preview
fun App() {
    CourtlyTheme {
        CompositionLocalProvider(LocalDimensions provides Dimensions()) {
            AppNavigator()
        }
    }
}
