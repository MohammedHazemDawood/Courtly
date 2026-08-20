package com.mhd_07.courtly.feature_nav.presentation

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable

@SuppressLint("SourceLockedOrientationActivity")
@Composable
actual fun ForcePortrait() {
    val activity = requireNotNull(LocalActivity.current){ return }
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}