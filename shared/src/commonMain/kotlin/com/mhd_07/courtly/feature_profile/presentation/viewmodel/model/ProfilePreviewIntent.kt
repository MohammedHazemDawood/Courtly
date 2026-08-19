package com.mhd_07.courtly.feature_profile.presentation.viewmodel.model

import com.mhd_07.courtly.core.domain.model.Player

sealed interface ProfilePreviewIntent {
    data class LoadProfileById(val id: String) : ProfilePreviewIntent
    data class LoadProfileByHandle(val handle: String) : ProfilePreviewIntent
    object Refresh : ProfilePreviewIntent

    data class Follow(val player: Player) : ProfilePreviewIntent
    data class Unfollow(val player: Player) : ProfilePreviewIntent
    data object LogOut : ProfilePreviewIntent

}