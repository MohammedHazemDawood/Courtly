package com.mhd_07.courtly.feature_sign.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.mhd_07.courtly.core.presentation.components.Loading
import com.mhd_07.courtly.core.presentation.ui.theme.CourtlyTheme
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.feature_nav.presentation.data.Graphs
import com.mhd_07.courtly.feature_sign.presentation.module.SignError
import com.mhd_07.courtly.feature_sign.presentation.module.SignIntent
import com.mhd_07.courtly.feature_sign.presentation.module.SignResult
import com.mhd_07.courtly.feature_sign.presentation.viewmodel.SignViewmodel
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.loading
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignUI() {
    val backStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(
                        Graphs.Sign.MailPasswordSign::class,
                        Graphs.Sign.MailPasswordSign.serializer()
                    )
                    subclass(Graphs.Sign.SignOptions::class, Graphs.Sign.SignOptions.serializer())
                    subclass(Graphs.Sign.OTP::class, Graphs.Sign.OTP.serializer())
                }
            }
        }, Graphs.Sign.SignOptions
    );

    val viewmodel = koinViewModel<SignViewmodel>()
    val state by viewmodel.state.collectAsStateWithLifecycle()
    val googleClient = viewmodel.google

    val hostState = remember { SnackbarHostState() }

    LaunchedEffect(state.result) {
        println("result: ${state.result}")
        if (state.result is SignResult.Error && (state.result as SignResult.Error).error == SignError.NotConfirmed && backStack.last() != Graphs.Sign.OTP) {
            viewmodel.handleIntent(SignIntent.ResendOTP)
            backStack.add(Graphs.Sign.OTP)
        } else if (state.result is SignResult.Error)
            hostState.showSnackbar(message = getString((state.result as SignResult.Error).error.message))
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = hostState) }) {
        if (state.result is SignResult.Loading)
            Loading()
        NavDisplay(
            backStack = backStack,
            modifier = Modifier.fillMaxSize().padding(it)
                .padding(WindowInsets.ime.asPaddingValues()),
            entryProvider = entryProvider {
                entry<Graphs.Sign.MailPasswordSign> {
                    MailPasswordSignScreen(
                        navBack = {
                            if (backStack.size > 1)
                                backStack.removeLast()
                        },
                        mail = state.email,
                        password = state.password,
                        onMailChange = { viewmodel.handleIntent(SignIntent.EditEmail(it)) },
                        onPasswordChange = { viewmodel.handleIntent(SignIntent.EditPassword(it)) },
                        onConfirm = { viewmodel.handleIntent(SignIntent.Sign) }
                    )
                }
                entry<Graphs.Sign.SignOptions> {
                    SignOptionsScreen(
                        googleSignIn = { googleClient.startFlow() },
                        navToEmailSign = { backStack.add(Graphs.Sign.MailPasswordSign) })
                }
                entry<Graphs.Sign.OTP> {
                    OTPScreen(
                        done = { viewmodel.handleIntent(SignIntent.VerifyOTP) },
                        email = state.email,
                        otp = state.otp,
                        onOtpChange = {
                            viewmodel.handleIntent(SignIntent.EditOTP(it))
                        })
                }
            })
    }
}