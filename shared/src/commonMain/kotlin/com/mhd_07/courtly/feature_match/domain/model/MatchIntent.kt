package com.mhd_07.courtly.feature_match.domain.model

import com.mhd_07.courtly.core.domain.model.Player

sealed interface MatchIntent {
    object Undo : MatchIntent
    object Redo : MatchIntent
    data class Team1Point(val player: Player) : MatchIntent
    data class Team2Point(val player: Player) : MatchIntent
}