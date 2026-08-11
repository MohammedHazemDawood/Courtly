package com.mhd_07.courtly.core.util

import androidx.compose.runtime.Composable

@Composable
expect fun rememberGalleryManager(onResult: (SharedImage) -> Unit): GalleryManager

class GalleryManager(
    private val onLaunch: () -> Unit
) {
    fun launch() {
        onLaunch.invoke()
    }
}