package com.mhd_07.courtly.feature_match_setup.presentation.model

import com.mhd_07.courtly.core.domain.model.MatchMode
import com.mhd_07.courtly.core.domain.model.MatchType
import com.mhd_07.courtly.core.domain.model.Player

sealed interface MatchSetupIntent {
    data class ChangeTeamLeftName(val newName: String) : MatchSetupIntent
    data class ChangeTeamRightName(val newName: String) : MatchSetupIntent

    data class AddTeamLeftPlayer(val player: Player) : MatchSetupIntent
    data class AddTeamRightPlayer(val player: Player) : MatchSetupIntent

    data class RemoveTeamLeftPlayer(val player: Player) : MatchSetupIntent
    data class RemoveTeamRightPlayer(val player: Player) : MatchSetupIntent

    data class ChangeLocation(val newLocation: String) : MatchSetupIntent

    data class ChangeBestOf(val newBestOf: Int) : MatchSetupIntent
    data class ChangeMode(val newMode: MatchMode) : MatchSetupIntent
    data class ChangeType(val newType: MatchType) : MatchSetupIntent

    data class SearchPlayers(val query: String) : MatchSetupIntent

    data object SetupMatch : MatchSetupIntent
}