package com.mhd_07.courtly.feature_match_setup.presentation.model

import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.presentation.model.RemoteResult
import com.mhd_07.courtly.feature_match_setup.domain.model.Setup

data class MatchSetupState(
    val setup: Setup = Setup(),
    val result : RemoteResult? = null,
    val searchQuery : String = "",
    val searchResults : List<Player> = emptyList(),
    val matchId : String? = null
)