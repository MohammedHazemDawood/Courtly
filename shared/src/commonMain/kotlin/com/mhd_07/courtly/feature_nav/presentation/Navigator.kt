package com.mhd_07.courtly.feature_nav.presentation

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
import androidx.savedstate.serialization.SavedStateConfiguration
import com.mhd_07.courtly.core.presentation.screens.CoreUI
import com.mhd_07.courtly.core.presentation.ui.theme.popTransform
import com.mhd_07.courtly.core.presentation.ui.theme.predictiveTransform
import com.mhd_07.courtly.core.presentation.ui.theme.pushTransform
import com.mhd_07.courtly.core.presentation.viewmodel.HANDLE_REGEX
import com.mhd_07.courtly.feature_match.presentation.screens.MatchUI
import com.mhd_07.courtly.feature_match_setup.presentation.screens.MatchSetupUI
import com.mhd_07.courtly.feature_nav.presentation.data.Graphs
import com.mhd_07.courtly.feature_nav.presentation.data.UserSelectionType
import com.mhd_07.courtly.feature_nav.presentation.viemodel.NavViewModel
import com.mhd_07.courtly.feature_profile.presentation.screen.ProfilePreviewUI
import com.mhd_07.courtly.feature_sign.presentation.screen.SignUI
import io.github.jan.supabase.auth.status.SessionStatus
import io.ktor.http.Url
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.viewmodel.koinViewModel

/**@pram :deepsLink https://courtly.app/{handle} **/
@Composable
fun AppNavigator(deepsLink: String? = null) {
    val backStack = rememberNavBackStack(configuration = SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(NavKey::class) {
                subclass(Graphs.Core::class, Graphs.Core.serializer())
                subclass(Graphs.Sign::class, Graphs.Sign.serializer())
                subclass(Graphs.Match::class, Graphs.Match.serializer())
                subclass(Graphs.Splash::class, Graphs.Splash.serializer())
                subclass(Graphs.MatchSetup::class, Graphs.MatchSetup.serializer())
                subclass(Graphs.ProfilePreview::class, Graphs.ProfilePreview.serializer())
            }
        }
    }, Graphs.Splash)

    val viewModel: NavViewModel = koinViewModel()
    val authState = viewModel.status.collectAsStateWithLifecycle()

    LaunchedEffect(authState.value, deepsLink) {
        println("AuthState: ${authState.value}")
        if (authState.value is SessionStatus.Authenticated && backStack.lastOrNull() == Graphs.Splash || backStack.lastOrNull() == Graphs.Sign) {
            println("Accepted")
            backStack.clear()
            backStack.add(Graphs.Core)
            if (deepsLink != null) {
                parseUrl(deepsLink)?.let {
                    backStack.add(Graphs.ProfilePreview(it, UserSelectionType.Handle))
                }
            }
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
                CoreUI(
                    navToGameSetup = { backStack.add(Graphs.MatchSetup) },
                    previewProfile = {
                        backStack.add(Graphs.ProfilePreview(it))
                    },
                    navToMatch = {
                        backStack.add(Graphs.Match(it))
                    },
                    setupScreen = {},
                    navToProfile = { backStack.add(Graphs.ProfilePreview(it, UserSelectionType.Id)) }
                )
            }
            entry<Graphs.Match> { key ->
                MatchUI(key.id) {
                    if (backStack.size > 1)
                        backStack.removeAt(backStack.lastIndex)
                }
            }
            entry<Graphs.Splash> {
                SplashScreen()
            }
            entry<Graphs.MatchSetup> {
                MatchSetupUI(
                    navBack = {
                        if (backStack.size > 1)
                            backStack.removeAt(backStack.lastIndex)
                    },
                    navToGameRecord = { id ->
                        backStack.removeAt(backStack.lastIndex)
                        backStack.add(Graphs.Match(id))
                    }
                )
            }
            entry<Graphs.ProfilePreview> { key ->
                ProfilePreviewUI(
                    key = key.key,
                    type = key.type,
                    navBack = {
                        if (backStack.size > 1)
                            backStack.removeAt(backStack.lastIndex)
                    },
                    navToMatch = {
                        backStack.add(Graphs.Match(it))
                    },
                )
            }
        }
    )
}

const val host = "courtly.app"
private fun parseUrl(link: String): String? {
    println("link: $link")
    val url = Url(link)
    if (url.host != host) return null
    val path = url.encodedPath.trim('/')
    return path.takeIf {
        println("path: $it")
        it.matches(Regex(HANDLE_REGEX))
    }
}