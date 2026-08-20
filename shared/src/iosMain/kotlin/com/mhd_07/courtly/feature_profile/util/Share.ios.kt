package com.mhd_07.courtly.feature_profile.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.BetaInteropApi
import platform.Foundation.NSString
import platform.Foundation.create
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

class IOSShareProvider : ShareProvider {
    @OptIn(BetaInteropApi::class)
    override fun shareProfile(handle: String, title: String) {
        val activityItems = listOf(
            NSString.create(string = "$title \n https://courtly.com/$handle")
        )
        val uiActivityController = UIActivityViewController(activityItems = activityItems, applicationActivities = null)
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(uiActivityController, true, null)
    }
}

@Composable
actual fun rememberShareProvider(): ShareProvider {
    return remember {
        IOSShareProvider()
    }
}