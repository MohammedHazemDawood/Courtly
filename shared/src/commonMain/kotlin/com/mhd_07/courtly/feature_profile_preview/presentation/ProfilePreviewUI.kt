package com.mhd_07.courtly.feature_profile_preview.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.feature_nav.presentation.data.UserSelectionType
import com.mhd_07.courtly.feature_profile_preview.presentation.viewmodel.ProfilePreviewViewModel
import com.mhd_07.courtly.feature_profile_preview.presentation.viewmodel.model.ProfilePreviewIntent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfilePreviewUI(
    key: String,
    type: UserSelectionType,
    navBack: () -> Unit,
    navToSettings: () -> Unit = {},
    previewProfile: (Player) -> Unit
) {
    val viewmodel: ProfilePreviewViewModel = koinViewModel()
    val state by viewmodel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        if (type == UserSelectionType.Id)
            viewmodel.handleIntent(ProfilePreviewIntent.LoadProfileById(key))
        else
            viewmodel.handleIntent(ProfilePreviewIntent.LoadProfileByHandle(key))
    }

    println("state: $state")

    ProfileScreen(
        navBack = navBack,
        profile = state.profile,
        followers = state.followers,
        following = state.following,
        myFollowers = state.userFollowers,
        myFollowing = state.userFollowing,
        result = state.result,
        onRefresh = { viewmodel.handleIntent(ProfilePreviewIntent.Refresh) },
        navToSettings = navToSettings,
        follow = { viewmodel.handleIntent(ProfilePreviewIntent.Follow(it)) },
        unfollow = { viewmodel.handleIntent(ProfilePreviewIntent.Unfollow(it)) },
        isMine = state.mine,
        previewProfile = previewProfile,
        myId = state.myId
    )
}

@Composable
fun ProfilePreviewUI(
    navBack: () -> Unit,
    navToSettings: () -> Unit = {},
    previewProfile: (Player) -> Unit
) {
    val viewmodel = koinViewModel<ProfilePreviewViewModel>()
    val state by viewmodel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewmodel.handleIntent(ProfilePreviewIntent.LoadMyProfile)
    }

    ProfileScreen(
        navBack = navBack,
        profile = state.profile,
        followers = state.followers,
        following = state.following,
        myFollowers = state.userFollowers,
        myFollowing = state.userFollowing,
        result = state.result,
        onRefresh = { viewmodel.handleIntent(ProfilePreviewIntent.Refresh) },
        navToSettings = navToSettings,
        follow = { viewmodel.handleIntent(ProfilePreviewIntent.Follow(it)) },
        unfollow = { viewmodel.handleIntent(ProfilePreviewIntent.Unfollow(it)) },
        isMine = state.mine,
        previewProfile = previewProfile,
        myId = state.myId
    )
}