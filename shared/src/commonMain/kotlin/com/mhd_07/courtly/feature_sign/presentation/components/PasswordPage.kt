package com.mhd_07.courtly.feature_sign.presentation.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mhd_07.courtly.core.presentation.ui.theme.CourtlyTheme
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.titleTextStyle
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.change_email
import courtly.shared.generated.resources.eye_closed_outline
import courtly.shared.generated.resources.eye_outline
import courtly.shared.generated.resources.pass
import courtly.shared.generated.resources.pass_placeholder
import courtly.shared.generated.resources.password_forgot
import courtly.shared.generated.resources.password_title
import org.jetbrains.compose.resources.painterResource


import org.jetbrains.compose.resources.stringResource

@Composable
fun PasswordPage(
    modifier: Modifier = Modifier,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordError: String? = null,
    email: String,
    onBackClick: () -> Unit,
    forgotPassword: () -> Unit
) {
    val dimensions = LocalDimensions.current
    var passwordVisible by remember { mutableStateOf(false) }
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
//        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.medium)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensions.xSmall)
        ) {
            Text(
                text = stringResource(Res.string.password_title),
                style = titleTextStyle,
                modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.spacedBy(
                    dimensions.xSmall,
                    Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = email)
                TextButton(onClick = onBackClick) {
                    Text(
                        text = stringResource(Res.string.change_email),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(dimensions.small)
        ) {
            Text(text = stringResource(Res.string.pass), style = titleTextStyle)
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = onPasswordChange,
                placeholder = { Text(text = stringResource(Res.string.pass_placeholder)) },
                isError = passwordError != null,
                supportingText = { Text(text = passwordError ?: "") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(if (passwordVisible) Res.drawable.eye_outline else Res.drawable.eye_closed_outline),
                            contentDescription = null //TODO : add description
                        )
                    }
                }
            )
            TextButton(onClick = forgotPassword) {
                Text(
                    text = stringResource(Res.string.password_forgot),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
@Preview
fun PasswordPagePreview() {
    CourtlyTheme {
        PasswordPage(
            password = "", onPasswordChange = {}, passwordError = null, email = "",
            onBackClick = {},
            forgotPassword = {}
        )
    }
}
