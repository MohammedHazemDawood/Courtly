package com.mhd_07.courtly.feature_nav.presentation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.defaultPredictivePopTransitionSpec
import androidx.savedstate.serialization.SavedStateConfiguration
import com.mhd_07.courtly.core.presentation.screens.CoreUI
import com.mhd_07.courtly.core.presentation.ui.theme.popTransform
import com.mhd_07.courtly.core.presentation.ui.theme.predictiveTransform
import com.mhd_07.courtly.core.presentation.ui.theme.pushTransform
import com.mhd_07.courtly.feature_match_record.presentation.screen.MatchUI
import com.mhd_07.courtly.feature_nav.presentation.data.Graphs
import com.mhd_07.courtly.feature_nav.presentation.viemodel.NavViewModel
import com.mhd_07.courtly.feature_profile_preview.presentation.ProfilePreviewUI
import com.mhd_07.courtly.feature_sign.presentation.screen.SignUI
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigator(deepsLink: String? = null) {


    val backStack = rememberNavBackStack(configuration = SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(NavKey::class) {
                subclass(Graphs.Core::class, Graphs.Core.serializer())
                subclass(Graphs.Sign::class, Graphs.Sign.serializer())
                subclass(Graphs.Match::class, Graphs.Match.serializer())
                subclass(Graphs.Splash::class, Graphs.Splash.serializer())
                subclass(Graphs.ProfilePreview::class, Graphs.ProfilePreview.serializer())
            }
        }
    }, Graphs.Splash)

    val viewModel: NavViewModel = koinViewModel()
    val authState = viewModel.status.collectAsStateWithLifecycle()


    LaunchedEffect(authState.value) {
        println("AuthState: ${authState.value}")
        if (authState.value is SessionStatus.Authenticated) {
            println("Accepted")
            backStack.clear()
            backStack.add(Graphs.Core)
        } else if (authState.value is SessionStatus.NotAuthenticated) {
            if (backStack.lastOrNull() != Graphs.Sign) {
                backStack.clear()
                backStack.add(Graphs.Sign)
            }
        }
    }

    NavDisplay(
        backStack = backStack,
        modifier = Modifier.fillMaxSize(),
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        transitionSpec = { pushTransform },
        popTransitionSpec = { popTransform },
        predictivePopTransitionSpec = { predictiveTransform },
        entryProvider = entryProvider {
            entry<Graphs.Sign> {
                SignUI()
            }
            entry<Graphs.Core> {
                CoreUI({ backStack.add(Graphs.Match) }) {
                    backStack.add(Graphs.ProfilePreview(it))
                }
            }
            entry<Graphs.Match> {
                MatchUI {
                    if (backStack.size > 1) {
                        backStack.clear()
                        backStack.add(Graphs.Core)
                    }
                }
            }
            entry<Graphs.Splash> {
                SplashScreen()
            }
            entry<Graphs.ProfilePreview>(

            ) { key ->
                ProfilePreviewUI(
                    id = key.id,
                    navBack = {
                        if (backStack.size > 1)
                            backStack.removeLast()
                    },
                    previewProfile = { player ->
                        backStack.add(Graphs.ProfilePreview(player.id))
                    }
                )
            }
        })
}