package com.mhd_07.courtly.core.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

@Composable
expect fun rememberCameraManager(onResult: (SharedImage) -> Unit): CameraManager

expect class SharedImage {
    fun toByteArray(): ByteArray?
    fun toImageBitmap(): ImageBitmap?
}

class CameraManager(
    private val onLaunch: () -> Unit
) {
    fun launch() {
        onLaunch.invoke()
    }
}