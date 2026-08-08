package com.mhd_07.courtly.feature_sign.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.tooling.preview.Preview
import com.mhd_07.courtly.core.presentation.components.CourtlyAppBar
import com.mhd_07.courtly.core.presentation.ui.theme.CourtlyTheme
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.buttonTextStyle
import com.mhd_07.courtly.core.util.BackHandler
import com.mhd_07.courtly.feature_sign.presentation.components.EmailPage
import com.mhd_07.courtly.feature_sign.presentation.components.PagerIndicator
import com.mhd_07.courtly.feature_sign.presentation.components.PasswordPage
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.continue_sign
import courtly.shared.generated.resources.email_error
import courtly.shared.generated.resources.pass_error
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

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage == 0)
            emailFocus.requestFocus()
        else
            passwordFocus.requestFocus()
    }

    BackHandler(scope) {
        if (pagerState.currentPage != 0)
            pagerState.animateScrollToPage(pagerState.currentPage - 1)
        else
            navBack()
    }
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CourtlyAppBar(title = "", backVisible = true, onBackClick = {
            if (pagerState.currentPage != 0)
                scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
            else
                navBack()
        })
    }) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(it)
                .padding(bottom = dimensions.large)
                .padding(horizontal = dimensions.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensions.medium)
        ) {
            PagerIndicator(
                modifier = Modifier.fillMaxWidth(0.5f),
                stepsCount = pagerState.pageCount,
                currentStep = pagerState.currentPage
            )
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                userScrollEnabled = false
            ) { page ->
                when (page) {
                    0 -> {
                        EmailPage(
                            modifier = Modifier.fillMaxSize(),
                            email = mail,
                            onEmailChange = onMailChange,
                            emailError = emailError
                        )
                    }

                    1 -> {
                        PasswordPage(
                            modifier = Modifier.fillMaxSize(),
                            password = password,
                            onPasswordChange = onPasswordChange,
                            passwordError = passError,
                            email = mail,
                            onBackClick = {
                                if (pagerState.currentPage != 0)
                                    scope.launch { pagerState.animateScrollToPage(0) }
                            },
                            forgotPassword = {/* TODO:Implement Forgot Password */ }
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
            }, shape = MaterialTheme.shapes.medium, enabled = nextEnabled) {
                Text(
                    text = stringResource(Res.string.continue_sign),
                    style = buttonTextStyle,
                    modifier = Modifier.padding(vertical = dimensions.xxSmall)
                )
            }
        }
    }
}

@Preview
@Composable
fun MailPasswordSignScreen() {
    CourtlyTheme(darkTheme = true) {
        MailPasswordSignScreen(
            navBack = {},
            mail = "",
            password = "",
            onMailChange = {},
            onPasswordChange = {},
            onConfirm = {}
        )
    }
}