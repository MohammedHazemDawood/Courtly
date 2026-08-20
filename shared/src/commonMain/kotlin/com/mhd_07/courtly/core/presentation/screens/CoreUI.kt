package com.mhd_07.courtly.core.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.mhd_07.courtly.core.presentation.model.CoreIntent
import com.mhd_07.courtly.core.presentation.ui.theme.popTransform
import com.mhd_07.courtly.core.presentation.ui.theme.predictiveTransform
import com.mhd_07.courtly.core.presentation.ui.theme.pushTransform
import com.mhd_07.courtly.core.presentation.viewmodel.CoreViewmodel
import com.mhd_07.courtly.feature_nav.presentation.data.Graphs
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CoreUI(navToGameSetup: () -> Unit, previewProfile: (id: String) -> Unit, navToMatch: (String) -> Unit, setupScreen: (id: String) -> Unit, navToProfile: (String) -> Unit) {
    val backStack = rememberNavBackStack(SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(NavKey::class) {
                subclass(Graphs.Core.Home::class, Graphs.Core.Home.serializer())
            }
        }
    }, Graphs.Core.Home)
    val viewmodel = koinViewModel<CoreViewmodel>()
    val state by viewmodel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        state.profile?.let {
            if (it.handle.isNullOrEmpty()) {
                state.profile?.id?.let { setupScreen(it) }
            }
        }
    }

    println("Core State: $state")
    NavDisplay(
        backStack = backStack,
        modifier = Modifier.fillMaxSize(),
        transitionSpec = { pushTransform },
        popTransitionSpec = { popTransform },
        predictivePopTransitionSpec = { predictiveTransform },
        entryProvider = entryProvider {
            entry<Graphs.Core.Home> {
                HomeScreen(
                    navToGameSetup = navToGameSetup,
                    navToProfileScreen = {state.profile?.id?.let { previewProfile(it) }},
                    userPFP = state.profile?.avatar + "?v=" + state.profile?.avatarVersion,
                    matches = state.matches,
                    navToMatch = navToMatch,
                    loadNext = { viewmodel.handleIntent(CoreIntent.LoadFeed) },
                    refresh = { viewmodel.handleIntent(CoreIntent.Refresh) },
                    result = state.result
                )
            }
        }
    )
}