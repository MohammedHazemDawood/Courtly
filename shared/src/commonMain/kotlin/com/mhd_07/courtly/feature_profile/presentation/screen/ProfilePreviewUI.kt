package com.mhd_07.courtly.feature_profile.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.mhd_07.courtly.feature_nav.presentation.data.Graphs
import com.mhd_07.courtly.feature_nav.presentation.data.UserSelectionType
import com.mhd_07.courtly.feature_profile.presentation.viewmodel.ProfileEditViewmodel
import com.mhd_07.courtly.feature_profile.presentation.viewmodel.ProfilePreviewViewModel
import com.mhd_07.courtly.feature_profile.presentation.viewmodel.model.ProfileEditIntent
import com.mhd_07.courtly.feature_profile.presentation.viewmodel.model.ProfilePreviewIntent
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.handle_error
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfilePreviewUI(
    key: String,
    type: UserSelectionType,
    navBack: () -> Unit,
    navToMatch: (String) -> Unit
) {
    val backStack = rememberNavBackStack(SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(NavKey::class) {
                subclass(
                    Graphs.ProfilePreview.Profile::class,
                    Graphs.ProfilePreview.Profile.serializer()
                )
                subclass(
                    Graphs.ProfilePreview.Settings::class,
                    Graphs.ProfilePreview.Settings.serializer()
                )
                subclass(
                    Graphs.ProfilePreview.EditProfile::class,
                    Graphs.ProfilePreview.EditProfile.serializer()
                )
            }
        }
    }, Graphs.ProfilePreview.Profile(id = key))

    NavDisplay(backStack = backStack, entryProvider = entryProvider {
        entry<Graphs.ProfilePreview.Profile> { profileKey ->
            // Scope ViewModel per nav entry using the route ID as a key
            val viewModel: ProfilePreviewViewModel = koinViewModel(key = profileKey.id)
            val state by viewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(profileKey.id) {
                if (type == UserSelectionType.Handle && profileKey.id == key) {
                    viewModel.handleIntent(ProfilePreviewIntent.LoadProfileByHandle(profileKey.id))
                } else {
                    profileKey.id?.let {
                        viewModel.handleIntent(
                            ProfilePreviewIntent.LoadProfileById(
                                it
                            )
                        )
                    }
                }
            }

            ProfileScreen(
                navBack = {
                    if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) else navBack()
                },
                profile = state.profile,
                followers = state.followers,
                following = state.following,
                myFollowers = state.userFollowers,
                myFollowing = state.userFollowing,
                result = state.result,
                onRefresh = { viewModel.handleIntent(ProfilePreviewIntent.Refresh) },
                navToSettings = { backStack.add(Graphs.ProfilePreview.Settings) },
                follow = { viewModel.handleIntent(ProfilePreviewIntent.Follow(it)) },
                unfollow = { viewModel.handleIntent(ProfilePreviewIntent.Unfollow(it)) },
                isMine = state.mine,
                // Fixed: Pass the player ID instead of the whole Player object
                previewProfile = { player ->
                    player.id.let { id ->
                        backStack.add(Graphs.ProfilePreview.Profile(id = id))
                    }
                },
                myId = state.myId,
                matches = state.matches,
                navToMatch = navToMatch
            )
        }

        entry<Graphs.ProfilePreview.Settings> {
            val rootViewModel: ProfilePreviewViewModel = koinViewModel(key = key)

            SettingsScreen(
                navBack = {
                    if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) else navBack()
                },
                logout = { rootViewModel.handleIntent(ProfilePreviewIntent.LogOut) },
                navToEditProfile = { backStack.add(Graphs.ProfilePreview.EditProfile) }
            )
        }

        entry<Graphs.ProfilePreview.EditProfile> {
            val rootViewModel: ProfileEditViewmodel = koinViewModel(key = key)
            val state by rootViewModel.state.collectAsStateWithLifecycle()

            val avatarUrl = state.avatarPath?.let { "$it?v=${state.avatarVersion}" }
            val coverUrl = state.profile?.cover?.let { "$it?v=${state.profile?.coverVersion ?: 0}" }

            EditProfileScreen(
                navBack = {
                    if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) else navBack()
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
    })
}