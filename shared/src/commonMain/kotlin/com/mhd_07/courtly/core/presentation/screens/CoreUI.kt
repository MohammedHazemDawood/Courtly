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
import com.mhd_07.courtly.feature_profile_preview.presentation.screen.ProfilePreviewUI
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.handle_error
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CoreUI(navToGameSetup: () -> Unit, previewProfile: (id: String) -> Unit) {
    val backStack = rememberNavBackStack(SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(NavKey::class) {
                subclass(Graphs.Core.Home::class, Graphs.Core.Home.serializer())
                subclass(Graphs.Core.Profile::class, Graphs.Core.Profile.serializer())
                subclass(Graphs.Core.Settings::class, Graphs.Core.Settings.serializer())
                subclass(Graphs.Core.EditProfile::class, Graphs.Core.EditProfile.serializer())
            }
        }
    }, Graphs.Core.Home)
    val viewmodel = koinViewModel<CoreViewmodel>()
    val state by viewmodel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        state.profile?.let {
            if (it.handle.isNullOrEmpty()) {
                backStack.clear()
                backStack.add(Graphs.Core.EditProfile)
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
                    navToProfileScreen = { backStack.add(Graphs.Core.Profile) },
                    userPFP = state.avatarPath + "?v=" + state.avatarVersion
                )
            }
            entry<Graphs.Core.Profile> {
                ProfilePreviewUI(
                    navBack = {
                        if (backStack.size > 1)
                            backStack.removeLast()
                    },
                    navToSettings = {
                        backStack.add(Graphs.Core.Settings)
                    },
                    previewProfile = { player ->  previewProfile(player.id) }

                )
            }
            entry<Graphs.Core.Settings> {
                SettingsScreen(
                    navBack = {
                        if (backStack.size > 1)
                            backStack.removeLast()
                    },
                    logout = {
                        viewmodel.handleIntent(CoreIntent.LogOut)
                    },
                    navToEditProfile = {
                        backStack.add(Graphs.Core.EditProfile)
                    }
                )
            }
            entry<Graphs.Core.EditProfile> {
                EditProfileScreen(
                    navBack = {
                        if (backStack.size > 1)
                            backStack.removeLast()
                    },
                    save = {
                        viewmodel.handleIntent(CoreIntent.UpdateProfile)
                    },
                    saveEnables = state.saveEnabled,
                    avatar = state.avatarPath + "?v=" + state.avatarVersion,
                    changeAvatar = {
                        viewmodel.handleIntent(CoreIntent.ChangeAvatar(it))
                    },
                    name = state.displayName,
                    onNameChange = { viewmodel.handleIntent(CoreIntent.ChangeName(it)) },
                    handle = state.handle,
                    onHandleChange = { viewmodel.handleIntent(CoreIntent.ChangeHandle(it)) },
                    handleErrorMessage = if (state.handle.isEmpty() || !state.handleAvailable) stringResource(
                        Res.string.handle_error
                    ) else null,
                    bio = state.bio,
                    onBioChange = { viewmodel.handleIntent(CoreIntent.ChangeBio(it)) },
                    result = state.result,
                    cover = state.profile?.cover + "",
                    changeCover = {
                        viewmodel.handleIntent(CoreIntent.ChangeCover(it))
                    }
                )
            }
        }
    )
}