package com.mhd_07.courtly.feature_nav.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.foreground_ico
import org.jetbrains.compose.resources.painterResource

@Composable
fun SplashScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(Res.drawable.foreground_ico),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(0.6f)
//               modifier = Modifier.size(100.dp)
            )
        }
    }
}