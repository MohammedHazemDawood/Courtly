package com.mhd_07.courtly.feature_match.domain.model

import com.mhd_07.courtly.core.domain.model.Player

data class Team(
    val name: String = "",
    val players: List<Player> = emptyList()
)