package com.mhd_07.courtly.core.domain.model

import androidx.compose.ui.graphics.Color

data class Team(
    val name: String = "",
    val color: Color = Color.Unspecified,
//    val prevWins: List<Boolean> = emptyList(),
    val players: List<Player> = emptyList(),
) {
    companion object {
        val initial = Team(
            name = "Any Team",
            color = Color.Unspecified,
//            prevWins = emptyList(),
            players = emptyList(),
        )
    }
}