package com.mhd_07.courtly.feature_sign.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.buttonTextStyle
import com.mhd_07.courtly.core.presentation.ui.theme.fieldsTextStyle
import com.mhd_07.courtly.core.presentation.ui.theme.notesTextStyle
import com.mhd_07.courtly.core.presentation.ui.theme.titleTextStyle
import com.mhd_07.courtly.core.util.BackHandler
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.continue_sign
import courtly.shared.generated.resources.email
import courtly.shared.generated.resources.email_error
import courtly.shared.generated.resources.email_placeholder
import courtly.shared.generated.resources.pass
import courtly.shared.generated.resources.pass_error
import courtly.shared.generated.resources.pass_placeholder
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.filled.AlertCircle
import dev.seyfarth.tablericons.filled.Eye
import dev.seyfarth.tablericons.outlined.EyeOff
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun MailPasswordSignScreen(
    navBack: () -> Unit,
    mail: String,
    password: String,
    onMailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirm: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState { 2 }
    val dimensions = LocalDimensions.current
    val emailFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }
    val mailRegex = Regex("""^[\w.-]+@([\w-]+\.)+[\w-]{2,}$""")
    val passRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$")


    val emailError =
        if (mailRegex.matches(mail) || mail.isEmpty()) null else stringResource(Res.string.email_error)
    val passError =
        if (passRegex.matches(password) || password.isEmpty()) null else stringResource(Res.string.pass_error)

    val nextEnabled = when (pagerState.currentPage) {
        0 -> mail.isNotEmpty() && emailError == null
        1 -> password.isNotEmpty() && passError == null
        else -> false
    }


    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage == 0)
            emailFocus.requestFocus()
        else
            passwordFocus.requestFocus()
    }

    BackHandler(scope) {
        println("Match Setup Screen: Back pressed, current page is num ${pagerState.currentPage}")
        if (pagerState.currentPage != 0)
            pagerState.animateScrollToPage(pagerState.currentPage - 1)
        else
            navBack()
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(WindowInsets.ime.asPaddingValues())
            .padding(vertical = dimensions.large, horizontal = dimensions.small),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            userScrollEnabled = false
        ) {
            when (it) {
                0 -> {
                    SingleTextPage(
                        modifier = Modifier.fillMaxSize(),
                        title = stringResource(Res.string.email),
                        label = stringResource(Res.string.email_placeholder),
                        value = mail,
                        onChange = onMailChange,
                        next = {
                            if (nextEnabled)
                                scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                        }, //TODO: Change Action Entirly to be Done
                        focusRequester = emailFocus,
                        error = emailError,
                        type = KeyboardType.Email,
                    )
                }

                1 -> {
                    SingleTextPage(
                        modifier = Modifier.fillMaxSize(),
                        title = stringResource(Res.string.pass),
                        label = stringResource(Res.string.pass_placeholder),
                        value = password,
                        onChange = onPasswordChange,
                        next = { if (nextEnabled) onConfirm() },
                        focusRequester = passwordFocus,
                        type = KeyboardType.Password,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        error = passError,
                        leadIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) TablerIcons.Outlined.EyeOff else TablerIcons.Filled.Eye,
                                    contentDescription = "Error",
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    )
                }
            }
        }
        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            if (pagerState.currentPage != pagerState.pageCount - 1)
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            else
                onConfirm()
        }, enabled = nextEnabled) {
            Text(text = stringResource(Res.string.continue_sign), style = buttonTextStyle)
        }
    }
}

@Composable
fun SingleTextPage(
    modifier: Modifier,
    title: String,
    label: String,
    value: String,
    onChange: (String) -> Unit,
    next: () -> Unit = {},
    focusRequester: FocusRequester = FocusRequester(),
    type: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    error: String? = null,
    leadIcon: @Composable (() -> Unit) = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.xSmall)
    ) {
        Text(
            title,
            style = titleTextStyle
        )
        BasicTextField(
            value = value,
            onValueChange = onChange,
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = type),
            keyboardActions = KeyboardActions(onNext = {
                if (value.isNotEmpty()) {
                    next()
                }
            }),
            visualTransformation = visualTransformation,
            maxLines = 2,
            textStyle = fieldsTextStyle.copy(color = MaterialTheme.colorScheme.onBackground),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.small)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(/*modifier = Modifier.fillMaxSize()*/) {
                            if (value.isEmpty())
                                Text(
                                    text = label,
                                    style = fieldsTextStyle.copy(color = Color.Gray)
                                )
                            it()
                        }
                        leadIcon()
                    }
                    if (error != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(LocalDimensions.current.xSmall)
                        ) {
                            Icon(
                                imageVector = TablerIcons.Filled.AlertCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = error,
                                style = notesTextStyle.copy(color = MaterialTheme.colorScheme.error)
                            )
                        }
                    }
                }
            }
        )
    }
}

