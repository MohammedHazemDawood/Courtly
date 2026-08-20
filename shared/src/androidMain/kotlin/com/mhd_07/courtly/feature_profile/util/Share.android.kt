package com.mhd_07.courtly.feature_profile.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import org.jetbrains.compose.resources.getString
import androidx.core.net.toUri


internal class AndroidShareProvider(private val context: Context) : ShareProvider {

    override fun shareProfile(handle: String, title: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "$title \n https://courtly.app/$handle")
            putExtra(Intent.EXTRA_TITLE, title)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(intent, null))
    }
}

@Composable
actual fun rememberShareProvider(): ShareProvider {
    val context = LocalContext.current
    return remember {
        AndroidShareProvider(context)
    }
}