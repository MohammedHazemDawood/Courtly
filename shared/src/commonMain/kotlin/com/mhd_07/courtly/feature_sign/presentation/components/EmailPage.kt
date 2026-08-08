package com.mhd_07.courtly.feature_sign.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mhd_07.courtly.core.presentation.ui.theme.CourtlyTheme
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.notesTextStyle
import com.mhd_07.courtly.core.presentation.ui.theme.titleTextStyle
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.email
import courtly.shared.generated.resources.email_description
import courtly.shared.generated.resources.email_placeholder
import courtly.shared.generated.resources.email_title
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.filled.Mail
import org.jetbrains.compose.resources.stringResource

@Composable
fun EmailPage(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChange: (String) -> Unit,
    emailError: String? = null
) {
    val dimensions = LocalDimensions.current
    Column(
        modifier = modifier,
//        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.medium)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensions.xSmall)
        ) {
            Text(
                text = stringResource(Res.string.email_title),
                style = titleTextStyle,
                modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(Res.string.email_description),
                style = notesTextStyle,
                modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
            )
        }
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(dimensions.small)
        ) {
            Text(text = stringResource(Res.string.email), style = titleTextStyle)
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = onEmailChange,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                leadingIcon = {
                    Icon(
                        imageVector = TablerIcons.Filled.Mail,
                        contentDescription = null
                    )
                },
                singleLine = true,
                placeholder = { Text(text = stringResource(Res.string.email_placeholder)) },
                isError = emailError != null,
                supportingText = { Text(text = emailError ?: "") }
            )
        }
    }
}

@Composable
@Preview
fun EmailPagePreview() {
    CourtlyTheme {
        EmailPage(
            email = "", onEmailChange = {}, emailError = null
        )
    }
}