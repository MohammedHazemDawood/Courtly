package com.mhd_07.courtly.core.presentation.model

import com.mhd_07.courtly.core.domain.model.Player

sealed interface CoreIntent {
    data class ChangeName(val name: String) : CoreIntent
    data class ChangeBio(val bio: String) : CoreIntent
    data class ChangeHandle(val handle: String) : CoreIntent
    data class ChangeAvatar(val avatar: ByteArray) : CoreIntent
    data object UpdateProfile : CoreIntent
//    data object Refresh : CoreIntent

//    data class Follow(val player: Player) : CoreIntent
//    data class Unfollow(val player: Player) : CoreIntent

    data object LogOut : CoreIntent
}