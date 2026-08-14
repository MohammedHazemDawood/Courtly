package com.mhd_07.courtly.feature_match_record.presentation.viewmodel

import androidx.compose.ui.graphics.Color
import com.mhd_07.courtly.core.domain.model.MatchMode
import com.mhd_07.courtly.core.domain.model.MatchType
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.domain.model.Side

sealed interface MatchIntent {

    data class Point(val side: Side) : MatchIntent
    data class Transfer(val from: Side, val player1: Player, val player2: Player?) : MatchIntent
    data class Sub(val side: Side, val player1: Player, val player2: Player) : MatchIntent

    data class EditBallPlayer(val newBallPlayer: Int) :
        MatchIntent

    data class StartGame(val startingTeam: Side) : MatchIntent

    object Undo : MatchIntent
    object Redo : MatchIntent
}