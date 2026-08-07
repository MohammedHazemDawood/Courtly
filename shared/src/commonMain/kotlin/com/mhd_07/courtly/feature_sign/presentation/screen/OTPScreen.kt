package com.mhd_07.courtly.feature_sign.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.buttonTextStyle
import com.mhd_07.courtly.core.presentation.ui.theme.fieldsTextStyle
import com.mhd_07.courtly.core.presentation.ui.theme.notesTextStyle
import com.mhd_07.courtly.core.presentation.ui.theme.titleTextStyle
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.continue_sign
import courtly.shared.generated.resources.next
import courtly.shared.generated.resources.otp_description
import courtly.shared.generated.resources.otp_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun OTPScreen(done: () -> Unit, email: String, otp: String, onOtpChange: (String) -> Unit) {
    val dimensions = LocalDimensions.current
    val focusManager = LocalFocusManager.current
    LaunchedEffect(otp) {
        if (otp.length == 6)
            focusManager.clearFocus()
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = dimensions.xSmall, vertical = dimensions.medium),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(0.3f).fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensions.small)
            ) {
                Text(text = stringResource(Res.string.otp_title), style = titleTextStyle)
                Text(
                    text = stringResource(Res.string.otp_description, email),
                    style = notesTextStyle
                )
            }
            OtpField(
                otp = otp,
                onOtpChange = onOtpChange,
                modifier = Modifier.fillMaxWidth().fillMaxHeight(0.1f)
            )
        }
        Button(modifier = Modifier.fillMaxWidth(), onClick = done) {
            Text(text = stringResource(Res.string.next), style = buttonTextStyle)
        }
    }
}

@Composable
fun OtpField(
    otp: String,
    onOtpChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    length: Int = 6
) {
    BasicTextField(
        modifier = modifier,
        value = otp,
        onValueChange = onOtpChange,
        textStyle = fieldsTextStyle.copy(color = MaterialTheme.colorScheme.onBackground),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                LocalDimensions.current.xSmall,
                alignment = Alignment.CenterHorizontally
            )
        ) {
            repeat(length) { index ->
                val isFocused = otp.length == index
                val char = if (otp.length > index) otp[index] else ""
                OTPDigit(
                    digit = char.toString(),
                    isFocused = isFocused,
                    modifier = Modifier.aspectRatio(1f).weight(1f)
                )
            }
        }
    }
}

@Composable
internal fun OTPDigit(
    modifier: Modifier = Modifier,
    digit: String,
    isFocused: Boolean
) {
    val dimensions = LocalDimensions.current
    Box(
        modifier = modifier then Modifier.border(
            1.dp,
            if (isFocused) MaterialTheme.colorScheme.onBackground
            else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
            MaterialTheme.shapes.medium
        ), contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(dimensions.xSmall),
            text = digit,
            style = fieldsTextStyle.copy(color = MaterialTheme.colorScheme.onBackground),
            textAlign = TextAlign.Center
        )
        AnimatedVisibility(visible = isFocused && digit.isEmpty()) {
            Box(
                modifier = Modifier.height(dimensions.medium).width(2.dp)
                    .background(MaterialTheme.colorScheme.onBackground)
            )
        }
    }
}