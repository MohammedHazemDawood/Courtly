package com.mhd_07.courtly.feature_profile.util

import androidx.compose.runtime.Composable

interface ShareProvider {
    fun shareProfile(handle: String, title: String)
}

@Composable
expect fun rememberShareProvider(): ShareProvider