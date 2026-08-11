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

    data class AddPlayer(val player: Player, val side: Side) : MatchIntent
    data class RemovePlayer(val player: Player, val side: Side) : MatchIntent
    data class SearchPlayers(val query: String) : MatchIntent

    data class EditTeamName(val side: Side, val newName: String) : MatchIntent
    data class EditPlayerName(val side: Side, val index: Int, val newName: String) : MatchIntent

    data class EditLocation(val newLocation: String) : MatchIntent

    data class EditBallPlayer(val newBallPlayer: Int) :
        MatchIntent

    data class EditBestOf(val newBestOf: Int) : MatchIntent

    data class EditMode(val mode: MatchMode) : MatchIntent

    data class EditType(val type: MatchType) : MatchIntent

    data class EditTeamColor(val side: Side, val newColor : Color) : MatchIntent

    data class StartGame(val startingTeam: Side) : MatchIntent

    object Undo : MatchIntent
    object Redo : MatchIntent
}