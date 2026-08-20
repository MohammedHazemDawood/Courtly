package com.mhd_07.courtly.feature_sign.presentation.screen


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import com.mhd_07.courtly.core.presentation.ui.theme.CourtlyTheme
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.buttonTextStyle
import com.mhd_07.courtly.core.presentation.ui.theme.notesTextStyle
import com.mhd_07.courtly.core.presentation.ui.theme.titleTextStyle
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.app_name
import courtly.shared.generated.resources.email_password
import courtly.shared.generated.resources.foreground_ico
import courtly.shared.generated.resources.letter_outline
import courtly.shared.generated.resources.or
import courtly.shared.generated.resources.welcome
import courtly.shared.generated.resources.welcome_description
import courtly.shared.generated.resources.with_x
import courtly.shared.generated.resources.x_brand
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SignOptionsScreen(
    sign: () -> Unit,
    navToEmailSign: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val dimensions = LocalDimensions.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(it)
                .padding(vertical = dimensions.medium, horizontal = dimensions.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimensions.small)
            ) {
                Image(
                    painter = painterResource(Res.drawable.foreground_ico),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(0.4f)
//               modifier = Modifier.size(100.dp)
                )
                Text(
                    text = stringResource(Res.string.app_name),
                    style = titleTextStyle
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimensions.xSmall)
            ) {
                Text(
                    text = stringResource(Res.string.welcome),
                    style = titleTextStyle,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = stringResource(Res.string.welcome_description),
                    style = notesTextStyle,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimensions.small)
            ) {
                NativeSignButton(modifier = Modifier.fillMaxWidth(), sign = sign)
                SignButton(
                    modifier = Modifier.fillMaxWidth(),
                    icon = painterResource(Res.drawable.x_brand),
                    text = stringResource(Res.string.with_x),
                    backgroundColor = Color.Black,
                    tint = Color.White,
                    onClick = {/*TODO: Implement X Login*/ }
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = dimensions.medium),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall)
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color.Gray)
                    Text(text = stringResource(Res.string.or), style = notesTextStyle)
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color.Gray)
                }
                SignButton(
                    modifier = Modifier.fillMaxWidth(),
                    icon = painterResource(Res.drawable.letter_outline),
                    text = stringResource(Res.string.email_password),
                    onClick = navToEmailSign
                )
                Text(
                    text = buildAnnotatedString {
                        val linkStyle =
                            notesTextStyle.copy(color = MaterialTheme.colorScheme.primary)
                                .toSpanStyle()
                        append("By Continuing, you agree to our ")
                        pushStyle(linkStyle)
                        withLink(LinkAnnotation.Url("https://example.com/terms")) {//TODO: Add Link
                            append("Terms of Service")
                        }
                        pop()
                        append(" and ")
                        pushStyle(linkStyle)
                        withLink(LinkAnnotation.Url("https://example.com/privacy")) {//TODO: Add Link
                            append("Privacy Policy")
                        }
                        pop()
                    },
                    style = notesTextStyle,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(bottom = dimensions.medium)
                )
            }
        }
    }
}

@Composable
expect fun NativeSignButton(modifier: Modifier, sign: () -> Unit)

@Composable
fun SignButton(
    icon: Painter,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    border: BorderStroke? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        border = border
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.widthIn(min = maxWidth * 0.6f)/*.fillMaxWidth(0.6f)*/
                    .padding(vertical = LocalDimensions.current.xxSmall),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(LocalDimensions.current.small)
            ) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = tint ?: LocalContentColor.current,
                    modifier = Modifier.size(LocalDimensions.current.medium)
                )
                Text(
                    text = text,
                    style = buttonTextStyle,
                    color = tint ?: Color.Unspecified,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.StartEllipsis
                )
            }
        }
    }
}

@Preview
@Composable
fun SignOptionsScreenPreview() {
    CourtlyTheme(darkTheme = true) {
        SignOptionsScreen(
            sign = {},
            navToEmailSign = {},
            snackbarHostState = SnackbarHostState()
        )
    }
}