package com.mhd_07.courtly.feature_match.domain.model

import com.mhd_07.courtly.core.presentation.model.RemoteResult

data class MatchState(
    val match: Match = Match.initial,
    val undoEnabled: Boolean = false,
    val redoEnabled: Boolean = false,
    val result: RemoteResult? = RemoteResult.Loading
)
