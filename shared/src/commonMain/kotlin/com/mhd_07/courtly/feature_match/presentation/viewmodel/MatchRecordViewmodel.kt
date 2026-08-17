package com.mhd_07.courtly.feature_match.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhd_07.courtly.core.domain.model.MatchStatus
import com.mhd_07.courtly.core.presentation.model.RemoteError
import com.mhd_07.courtly.core.presentation.model.RemoteResult
import com.mhd_07.courtly.core.presentation.model.getPostgrestError
import com.mhd_07.courtly.feature_match.domain.model.*
import com.mhd_07.courtly.feature_match.domain.usecase.GetMatchUseCase
import com.mhd_07.courtly.feature_match.domain.usecase.UpdateMatchUseCase
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Clock

class MatchRecordViewmodel(
    private val update: UpdateMatchUseCase,
    getMatch: GetMatchUseCase,
    matchId: String,
) : ViewModel() {

    private val actionMutex = Mutex()
    private lateinit var timeLineManager: TimeLineManager

    private val _state = MutableStateFlow(MatchState())
    val state: StateFlow<MatchState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getMatch(matchId).let { match ->
                _state.update { it.copy(match = match) }
//                println("Match: $match")
            }
            timeLineManager = TimeLineManager(_state.value.match, ::reducer)
//            if (_state.value.match.status == MatchStatus.Coming) {
            _state.update {
                it.copy(
                    match = timeLineManager.start(),
                    undoEnabled = timeLineManager.undoAvailable,
                    redoEnabled = timeLineManager.redoAvailable
                )
            }
            syncToRemote(_state.value.match)
//            }
        }
    }

    fun handleIntent(intent: MatchIntent) {
        viewModelScope.launch {
            actionMutex.withLock {
                val updatedMatch = when (intent) {
                    MatchIntent.Undo -> timeLineManager.undo()
                    MatchIntent.Redo -> timeLineManager.redo()
                    is MatchIntent.Team1Point -> timeLineManager.pointTeam1(intent.player)
                    is MatchIntent.Team2Point -> timeLineManager.pointTeam2(intent.player)
                }
                syncToRemote(updatedMatch)
                _state.update {
                    it.copy(
                        match = updatedMatch,
                        undoEnabled = timeLineManager.undoAvailable,
                        redoEnabled = timeLineManager.redoAvailable
                    )
                }
            }
        }
    }

    private suspend fun syncToRemote(match: Match) {
        try {
            update(match)
        } catch (e: PostgrestRestException) {
            println("Error sync: ${e.message}")
            _state.update { it.copy(result = RemoteResult.Error(getPostgrestError(e.code))) }
        } catch (e: Exception) {
            println("Error sync: ${e.message}")
            _state.update { it.copy(result = RemoteResult.Error(RemoteError.Unknown)) }
        }
    }

    private fun reducer(state: Match, event: Event): Match {
        return when (event) {
            is Event.Team1Point -> state.pointTeam1()
            is Event.Team2Point -> state.pointTeam2()
            is Event.Team1GameWin -> state.winGameTeam1()
            is Event.Team2GameWin -> state.winGameTeam2()
            is Event.Team1SetWin -> state.winSetTeam1()
            is Event.Team2SetWin -> state.winSetTeam2()
            is Event.Team1Won -> state.winMatchTeam1()
            is Event.Team2Won -> state.winMatchTeam2()
            is Event.Start -> state.copy(
                status = MatchStatus.Live,
                startedAt = Clock.System.now(),
                sets = persistentListOf(Set()),
            ).sortPlayers()
        }
    }
}