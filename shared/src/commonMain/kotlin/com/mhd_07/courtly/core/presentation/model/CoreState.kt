package com.mhd_07.courtly.core.presentation.model

import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.feature_match.domain.model.Match

data class CoreState(
    val profile: Player? = null,
    val result: RemoteResult? = null,
    val page : Long = -1,
    val matches : List<Match> = emptyList()
)
