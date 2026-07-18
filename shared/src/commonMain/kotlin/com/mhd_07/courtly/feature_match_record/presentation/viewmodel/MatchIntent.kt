package com.mhd_07.courtly.feature_match_record.presentation.viewmodel

import androidx.compose.ui.graphics.Color
import com.mhd_07.courtly.core.domain.model.Side

sealed interface MatchIntent {

    data class Point(val side: Side) : MatchIntent
    data class Transfer(val from: Side, val indexFrom: Int) : MatchIntent
    data class Sub(val from: Side, val indexFrom: Int, val indexTo: Int) : MatchIntent

    data class EditTeamName(val side: Side, val newName: String) : MatchIntent
    data class EditPlayerName(val side: Side, val index: Int, val newName: String) : MatchIntent

    data class EditBallPlayer(val side: Side, val index: Int, val newBallPlayer: Int?) :
        MatchIntent

    data class EditBestOf(val newBestOf: Int) : MatchIntent
    data class EditTeamColor(val side: Side, val newColor : Color) : MatchIntent

    data class StartGame(val startingTeam: Side) : MatchIntent

    object Undo : MatchIntent
    object Redo : MatchIntent
}