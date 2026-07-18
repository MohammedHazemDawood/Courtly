package com.mhd_07.courtly.core.domain.model

import androidx.compose.ui.graphics.Color

data class Team(
    val name: String,
    val color: Color,
    val currentScore: Score,
    val prevWins: List<Boolean>,
    val players: List<Player>,
    val ballPlayer: Int?,
) {
    companion object {
        val initial = Team(
            name = "Any Team",
            color = Color.Unspecified,
            currentScore = Score.Zero,
            prevWins = emptyList(),
            players = emptyList(),
            ballPlayer = null,
        )
    }
}