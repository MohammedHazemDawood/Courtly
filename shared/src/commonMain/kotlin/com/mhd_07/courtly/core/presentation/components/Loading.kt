package com.mhd_07.courtly.core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.loading
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.stringResource

@Composable
fun Loading() {
    Dialog(onDismissRequest = {}) {
        var json by remember { mutableStateOf("") }
        LaunchedEffect(Unit) {
            json = Res.readBytes(
                "files/load_animation.json"
            ).decodeToString()
        }
        val composition by rememberLottieComposition(LottieCompositionSpec.JsonString(json))
        val progress by animateLottieCompositionAsState(composition, iterations = Compottie.IterateForever)

//                Column(
//                    modifier = Modifier.fillMaxSize(0.5f),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.small)
//                ) {
        Image(
            painter = rememberLottiePainter(
                progress = { progress },
                composition = composition
            ),
            contentDescription = stringResource(Res.string.loading)
        )
//                    Text(text = stringResource(Res.string.loading))
//                }
    }
}