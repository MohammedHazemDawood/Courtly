package com.mhd_07.courtly

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mhd_07.courtly.core.ui.theme.CourtlyTheme

@Composable
@Preview
fun App() {
    CourtlyTheme {
        Surface(modifier = Modifier.fillMaxSize()){
            Box(modifier = Modifier.fillMaxSize(0.5f)) {
                Text(text = "Hello World")
            }
        }
    }
}