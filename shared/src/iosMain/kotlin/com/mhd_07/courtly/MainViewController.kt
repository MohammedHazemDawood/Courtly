package com.mhd_07.courtly

import androidx.compose.ui.window.ComposeUIViewController
import com.mhd_07.courtly.core.domain.di.initKoin
import io.github.jan.supabase.SupabaseClient
import org.koin.compose.koinInject
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    initKoin {}
    return ComposeUIViewController {
        print("supabase client ${koinInject<SupabaseClient>().supabaseUrl}")
        App()
    }
}