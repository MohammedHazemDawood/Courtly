package com.mhd_07.courtly.feature_nav.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.mhd_07.courtly.feature_profile.presentation.screen.EditProfileScreen
import com.mhd_07.courtly.feature_profile.presentation.screen.ProfilePreviewUI
import com.mhd_07.courtly.feature_profile.presentation.viewmodel.ProfileEditViewmodel
import com.mhd_07.courtly.feature_profile.presentation.viewmodel.model.ProfileEditIntent
import com.mhd_07.courtly.feature_sign.presentation.screen.SignUI
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.handle_error
import io.github.jan.supabase.auth.status.SessionStatus
import io.ktor.http.Url
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.jetbrains.compose.resources.stringResource
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
                subclass(Graphs.ProfileSetup::class, Graphs.ProfileSetup.serializer())
            }
        }
    }, Graphs.Splash)

    val viewModel: NavViewModel = koinViewModel()
    val authState = viewModel.status.collectAsStateWithLifecycle()

    ForcePortrait()

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
                    setupScreen = {
                        backStack.clear()
                        backStack.add(Graphs.ProfileSetup(it))
                    },
                    navToProfile = {
                        backStack.add(
                            Graphs.ProfilePreview(
                                it,
                                UserSelectionType.Id
                            )
                        )
                    }
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
            //TODO: Handle this using profile feature
            entry<Graphs.ProfileSetup> { key ->
                val rootViewModel: ProfileEditViewmodel = koinViewModel(key = key.id)
                val state by rootViewModel.state.collectAsStateWithLifecycle()

                val avatarUrl = state.avatarPath?.let { "$it?v=${state.avatarVersion}" }
                val coverUrl =
                    state.profile?.cover?.let { "$it?v=${state.profile?.coverVersion ?: 0}" }

                EditProfileScreen(
                    navBack = {
//                        if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) else navBack()
                    },
                    save = { rootViewModel.handleIntent(ProfileEditIntent.UpdateProfile) },
                    saveEnabled = state.saveEnabled,
                    avatar = avatarUrl ?: "",
                    changeAvatar = { rootViewModel.handleIntent(ProfileEditIntent.ChangeAvatar(it)) },
                    name = state.displayName,
                    onNameChange = { rootViewModel.handleIntent(ProfileEditIntent.ChangeName(it)) },
                    handle = state.handle,
                    onHandleChange = { rootViewModel.handleIntent(ProfileEditIntent.ChangeHandle(it)) },
                    handleErrorMessage = if (state.handle.isEmpty() || !state.handleAvailable) {
                        stringResource(Res.string.handle_error)
                    } else null,
                    bio = state.bio,
                    onBioChange = { rootViewModel.handleIntent(ProfileEditIntent.ChangeBio(it)) },
                    result = state.result,
                    cover = coverUrl ?: "",
                    onCoverChange = { rootViewModel.handleIntent(ProfileEditIntent.ChangeCover(it)) }
                )
            }
        }
    )
}

@Composable
expect fun ForcePortrait()

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