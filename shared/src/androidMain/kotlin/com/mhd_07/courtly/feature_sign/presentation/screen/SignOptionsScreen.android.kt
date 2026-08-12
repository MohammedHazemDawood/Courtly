package com.mhd_07.courtly.feature_sign.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.google_brand
import courtly.shared.generated.resources.with_google
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
actual fun NativeSignButton(modifier: Modifier, sign: () -> Unit) {
    SignButton(
        modifier = modifier,
        icon = painterResource(Res.drawable.google_brand),
        text = stringResource(Res.string.with_google),
        onClick = sign,
        tint = Color.Unspecified,
        backgroundColor = Color.White,
        border = BorderStroke(1.dp, Color.Black)
    )
}