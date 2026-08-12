package com.mhd_07.courtly.feature_sign.presentation.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.mhd_07.courtly.feature_nav.presentation.data.Graphs
import com.mhd_07.courtly.core.presentation.model.RemoteError
import com.mhd_07.courtly.feature_sign.presentation.model.SignIntent
import com.mhd_07.courtly.core.presentation.model.RemoteResult
import com.mhd_07.courtly.core.presentation.ui.theme.popTransform
import com.mhd_07.courtly.core.presentation.ui.theme.predictiveTransform
import com.mhd_07.courtly.core.presentation.ui.theme.pushTransform
import com.mhd_07.courtly.feature_sign.presentation.viewmodel.SignViewmodel
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.jetbrains.compose.resources.getString
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
    val nativeClient = viewmodel.native

    val hostState = remember { SnackbarHostState() }

    LaunchedEffect(state.result) {
        println("result: ${state.result}")
        if (state.result is RemoteResult.Error && (state.result as RemoteResult.Error).error == RemoteError.NotConfirmed && backStack.last() != Graphs.Sign.OTP) {
            viewmodel.handleIntent(SignIntent.ResendOTP)
            backStack.add(Graphs.Sign.OTP)
        } else if (state.result is RemoteResult.Error)
            hostState.showSnackbar(message = getString((state.result as RemoteResult.Error).error.message))
    }

    NavDisplay(
        backStack = backStack,
        modifier = Modifier.fillMaxSize(),/*.padding(it)*/
        transitionSpec = { pushTransform }, popTransitionSpec = { popTransform }, predictivePopTransitionSpec = {predictiveTransform},

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
                    onConfirm = { viewmodel.handleIntent(SignIntent.Sign) },
                    snackbarHostState = hostState
                )
            }
            entry<Graphs.Sign.SignOptions> {
                SignOptionsScreen(
                    sign = { nativeClient.startFlow() },
                    navToEmailSign = { backStack.add(Graphs.Sign.MailPasswordSign) },
                    snackbarHostState = hostState,
                )
            }
            //TODO: add Forgot Password Screen
            entry<Graphs.Sign.OTP> {
                OTPScreen(
                    done = {
                        viewmodel.handleIntent(SignIntent.VerifyOTP)
                        viewmodel.handleIntent(SignIntent.EditOTP(""))
                    },
                    email = state.email,
                    otp = state.otp,
                    onOtpChange = {
                        viewmodel.handleIntent(SignIntent.EditOTP(it))
                    }, navBack = {
                        if (backStack.size > 1)
                            backStack.removeLast()
                    }, snackbarHostState = hostState
                )
            }
        })
}