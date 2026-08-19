package com.mhd_07.courtly.feature_profile.presentation.viewmodel.model

import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.presentation.model.RemoteResult
import com.mhd_07.courtly.feature_match.domain.model.Match

data class ProfilePreviewState(
    val profile: Player? = null,
    val followers: List<Player> = emptyList(),
    val following: List<Player> = emptyList(),
    val userFollowers: List<Player> = emptyList(),
    val userFollowing: List<Player> = emptyList(),
    val result: RemoteResult? = null,
    val myId : String? = null,
    val mine : Boolean = false,
    val matches : List<Match> = emptyList()
)