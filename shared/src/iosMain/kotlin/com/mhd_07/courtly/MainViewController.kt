package com.mhd_07.courtly

import androidx.compose.ui.window.ComposeUIViewController
import com.mhd_07.courtly.core.domain.di.initKoin
import platform.UIKit.UIViewController

fun MainViewController() : UIViewController {
    initKoin {}
    return ComposeUIViewController { App() }
}