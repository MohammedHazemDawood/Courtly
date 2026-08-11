package com.mhd_07.courtly.feature_match_record.presentation.screen

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.mhd_07.courtly.core.presentation.ui.theme.popTransform
import com.mhd_07.courtly.core.presentation.ui.theme.predictiveTransform
import com.mhd_07.courtly.core.presentation.ui.theme.pushTransform
import com.mhd_07.courtly.feature_match_record.presentation.viewmodel.MatchIntent
import com.mhd_07.courtly.feature_match_record.presentation.viewmodel.MatchViewModel
import com.mhd_07.courtly.feature_nav.presentation.data.Graphs
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MatchUI(navBack: () -> Unit) {
    val backStack = rememberNavBackStack(configuration = SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(NavKey::class) {
                subclass(Graphs.Match.Setup::class, Graphs.Match.Setup.serializer())
                subclass(Graphs.Match.Record::class, Graphs.Match.Record.serializer())
            }
        }
    }, Graphs.Match.Setup)
    val viewmodel = koinViewModel<MatchViewModel>()
    val state by viewmodel.state.collectAsStateWithLifecycle()
    val isUndoAvailable by viewmodel.isUndoAvailable.collectAsStateWithLifecycle()
    val isRedoAvailable by viewmodel.isRedoAvailable.collectAsStateWithLifecycle()

    NavDisplay(
        backStack = backStack,
        modifier = Modifier.fillMaxSize(),
        transitionSpec = { pushTransform }, popTransitionSpec = { popTransform }, predictivePopTransitionSpec = { predictiveTransform },
        entryProvider = entryProvider {
            entry<Graphs.Match.Setup> {
                MatchSetupScreen(
                    navBack = navBack,
                    navToGameRecord = { backStack.add(Graphs.Match.Record) },
                    teamLeftName = state.teamLeft.name,
                    teamRightName = state.teamRight.name,
                    location = state.location,
                    type = state.type,
                    mode = state.mode,
                    bestOf = state.bestOf,
                    onSearch = { viewmodel.handleIntent(MatchIntent.SearchPlayers(it)) },
                    searchText = state.searchText,
                    searchResults = state.searchResults,
                    addPlayer = { player, side ->
                        viewmodel.handleIntent(MatchIntent.AddPlayer(player, side))
                    },
                    removePlayer = { player, side ->
                        viewmodel.handleIntent(MatchIntent.RemovePlayer(player, side))
                    },
                    onChangeName = { side, name ->
                        viewmodel.handleIntent(MatchIntent.EditTeamName(side, name))
                    },
                    onEditLocation = { location ->
                        viewmodel.handleIntent(MatchIntent.EditLocation(location))
                    },
                    onModeChange = { mode -> viewmodel.handleIntent(MatchIntent.EditMode(mode)) },
                    onBestOfChange = { bestOf ->
                        viewmodel.handleIntent(MatchIntent.EditBestOf(bestOf))
                    },
                    onTypeChange = { type -> viewmodel.handleIntent(MatchIntent.EditType(type)) },
                    startGame = { side -> viewmodel.handleIntent(MatchIntent.StartGame(side)) },
                    teamLeftPlayers = state.teamLeft.players,
                    teamRightPlayers = state.teamRight.players
                )
            }
            entry<Graphs.Match.Record> {
                MatchScreen(
                    state = state,
                    undoAvailable = isUndoAvailable,
                    redoAvailable = isRedoAvailable,
                    onUndo = { viewmodel.handleIntent(MatchIntent.Undo) },
                    onRedo = { viewmodel.handleIntent(MatchIntent.Redo) },
                    onPoint = { side -> viewmodel.handleIntent(MatchIntent.Point(side)) },
                    navBack = navBack
                )
            }
        })
}