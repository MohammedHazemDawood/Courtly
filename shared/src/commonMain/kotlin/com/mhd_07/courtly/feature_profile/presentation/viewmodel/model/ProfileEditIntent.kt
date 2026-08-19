package com.mhd_07.courtly.feature_profile.presentation.viewmodel.model

import com.mhd_07.courtly.core.domain.model.Player

sealed interface ProfileEditIntent {
    data class ChangeName(val name: String) : ProfileEditIntent
    data class ChangeBio(val bio: String) : ProfileEditIntent
    data class ChangeHandle(val handle: String) : ProfileEditIntent
    data class ChangeAvatar(val avatar: ByteArray) : ProfileEditIntent
    data class ChangeCover(val cover: ByteArray) : ProfileEditIntent
    data object UpdateProfile : ProfileEditIntent
}