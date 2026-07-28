package com.mhd_07.courtly.feature_sign.presentation.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.mhd_07.courtly.feature_nav.presentation.data.Graphs
import com.mhd_07.courtly.feature_sign.presentation.module.SignIntent
import com.mhd_07.courtly.feature_sign.presentation.module.SignResult
import com.mhd_07.courtly.feature_sign.presentation.viewmodel.SignViewmodel
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
                }
            }
        }, Graphs.Sign.SignOptions
    );

    val viewmodel = koinViewModel<SignViewmodel>()
    val state by viewmodel.state.collectAsStateWithLifecycle()
    val googleClient = viewmodel.google

    val hostState = remember { SnackbarHostState() }

    LaunchedEffect(state.result) {
        if (state.result is SignResult.Error)
            hostState.showSnackbar(message = getString((state.result as SignResult.Error).error.message))
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = hostState) }) {
        NavDisplay(
            backStack = backStack,
            modifier = Modifier.fillMaxSize().padding(it),
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
            })
    }
}