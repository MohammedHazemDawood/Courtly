package com.mhd_07.courtly.feature_profile_preview.presentation.viewmodel.model

import com.mhd_07.courtly.core.domain.model.Player

sealed interface ProfilePreviewIntent {
    data class LoadProfile(val id: String) : ProfilePreviewIntent
    data object LoadMyProfile : ProfilePreviewIntent
    object Refresh : ProfilePreviewIntent
    data class Follow(val player: Player) : ProfilePreviewIntent
    data class Unfollow(val player: Player) : ProfilePreviewIntent
}