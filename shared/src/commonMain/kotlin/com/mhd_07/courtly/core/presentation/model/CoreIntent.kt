package com.mhd_07.courtly.core.presentation.model

sealed interface CoreIntent {

    data object LoadFeed : CoreIntent
    data object Refresh : CoreIntent

//    data class Follow(val player: Player) : CoreIntent
//    data class Unfollow(val player: Player) : CoreIntent
}