package com.mhd_07.courtly.feature_match.data.model

import com.mhd_07.courtly.feature_match.domain.model.Game
import kotlinx.serialization.Serializable

@Serializable
data class RemoteSet(
    val team_1_games: Int,

    val team_2_games: Int,

    val current_game: Game,

    val games: List<Game>,
)