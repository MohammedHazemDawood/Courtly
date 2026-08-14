package com.mhd_07.courtly.feature_match_setup.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mhd_07.courtly.feature_match_setup.presentation.model.MatchSetupIntent
import com.mhd_07.courtly.feature_match_setup.presentation.viewmodel.MatchSetupViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MatchSetupUI(
    navBack: () -> Unit,
    navToGameRecord: (id: String?) -> Unit
) {
    val viewmodel = koinViewModel<MatchSetupViewModel>()
    val state by viewmodel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        println("State = $state")
    }

    MatchSetupScreen(
        teamLeftName = state.setup.teamLeft.name,
        teamRightName = state.setup.teamRight.name,
        teamLeftPlayers = state.setup.teamLeft.players,
        teamRightPlayers = state.setup.teamRight.players,
        searchText = state.searchQuery,
        onSearch = { viewmodel.handleIntent(MatchSetupIntent.SearchPlayers(it)) },
        searchResults = state.searchResults,
        addTeamLeftPlayer = { viewmodel.handleIntent(MatchSetupIntent.AddTeamLeftPlayer(it)) },
        addTeamRightPlayer = { viewmodel.handleIntent(MatchSetupIntent.AddTeamRightPlayer(it)) },
        removeTeamLeftPlayer = { viewmodel.handleIntent(MatchSetupIntent.RemoveTeamLeftPlayer(it)) },
        removeTeamRightPlayer = { viewmodel.handleIntent(MatchSetupIntent.RemoveTeamRightPlayer(it)) },
        location = state.setup.location,
        type = state.setup.type,
        mode = state.setup.mode,
        bestOf = state.setup.bestOf,
        navToGameRecord = { navToGameRecord(state.matchId) },
        navBack = navBack,
        onChangeTeamLeftName = { viewmodel.handleIntent(MatchSetupIntent.ChangeTeamLeftName(it)) },
        onChangeTeamRightName = { viewmodel.handleIntent(MatchSetupIntent.ChangeTeamRightName(it)) },
        onEditLocation = { viewmodel.handleIntent(MatchSetupIntent.ChangeLocation(it)) },
        onModeChange = { viewmodel.handleIntent(MatchSetupIntent.ChangeMode(it)) },
        onBestOfChange = { viewmodel.handleIntent(MatchSetupIntent.ChangeBestOf(it)) },
        onTypeChange = { viewmodel.handleIntent(MatchSetupIntent.ChangeType(it)) },
        startGame = { viewmodel.handleIntent(MatchSetupIntent.SetupMatch) },
        result = state.result,
    )
}