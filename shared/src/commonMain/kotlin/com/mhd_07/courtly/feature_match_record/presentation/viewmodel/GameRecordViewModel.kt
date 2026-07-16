package com.mhd_07.courtly.feature_match_record.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhd_07.courtly.core.domain.model.Match
import com.mhd_07.courtly.feature_match_record.domain.model.MatchIntent
import com.mhd_07.courtly.core.domain.model.MatchStatus
import com.mhd_07.courtly.core.domain.model.Score
import com.mhd_07.courtly.core.domain.model.Side
import com.mhd_07.courtly.core.domain.model.opposite
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class GameRecordViewModel : ViewModel() {
    private val _state = MutableStateFlow(Match.initial)
    val state = _state.asStateFlow()

    private val _undoStack = MutableStateFlow<ArrayDeque<MatchIntent.TimelineIntent>>(ArrayDeque())
    private val _redoStack = MutableStateFlow<ArrayDeque<MatchIntent.TimelineIntent>>(ArrayDeque())

    val isUndoAvailable = _undoStack.map { it.isNotEmpty() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )
    val isRedoAvailable = _redoStack.map { it.isNotEmpty() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    fun handleIntent(intent: MatchIntent) {
        when (intent) {
            MatchIntent.Redo -> redo()
            MatchIntent.Undo -> undo()
            is MatchIntent.TimelineIntent -> executeTimelineIntent(intent)
        }
    }

    private fun executeTimelineIntent(intent: MatchIntent.TimelineIntent) {
        when (intent) {
            is MatchIntent.TimelineIntent.Point -> executeScore(intent.side)
            is MatchIntent.TimelineIntent.Sub -> executeTransfer(
                from = intent.from,
                indexFrom = intent.indexFrom,
                indexTo = intent.indexTo
            )

            is MatchIntent.TimelineIntent.Transfer -> executeTransfer(
                from = intent.from,
                indexFrom = intent.indexFrom,
                indexTo = null
            )

            is MatchIntent.TimelineIntent.WinGame -> executeWinGame(intent.side)
            is MatchIntent.TimelineIntent.WinMatch -> executeMatchWin(intent.side)
        }
        addTimelineIntent(intent)
        if (intent !is MatchIntent.TimelineIntent.WinGame)
            checkGameWin()
    }

    private fun addTimelineIntent(intent: MatchIntent.TimelineIntent) {
        _undoStack.update {
            ArrayDeque(it).apply { addLast(intent) }
        }
        _redoStack.update { ArrayDeque() }
    }


    private fun executeScore(side: Side) {
        _state.update { if (side == Side.TeamRight) it.teamRightScore() else it.teamLeftScore() }
    }

    private fun executeTransfer(from: Side, indexFrom: Int, indexTo: Int?) {
        _state.update {
            it.sub(
                from = from,
                fromIndex = indexFrom,
                toIndex = indexTo
            )
        }
    }

    private fun executeWinGame(side: Side) {
        _state.update {
            it.run {
                copy(
                    teamRight = teamRight.copy(
                        currentScore = Score.Zero,
                        prevWins = teamRight.prevWins + (side == Side.TeamRight),
                        ballPlayer = null,
                    ),
                    teamLeft = teamLeft.copy(
                        currentScore = Score.Zero,
                        prevWins = teamLeft.prevWins + (side == Side.TeamLeft),
                        ballPlayer = null,
                    ),
                    ballTeam = ballTeam?.opposite(),
                )
            }
        }.also { checkMatchWin() }
    }

    private fun checkGameWin() {
        val rightScore = _state.value.teamRight.currentScore
        val leftScore = _state.value.teamLeft.currentScore

        if (rightScore == Score.Win) {
            executeTimelineIntent(MatchIntent.TimelineIntent.WinGame(Side.TeamRight))
        } else if (leftScore == Score.Win) {
            executeTimelineIntent(MatchIntent.TimelineIntent.WinGame(Side.TeamLeft))
        }
    }

    private fun executeMatchWin(side: Side) {
        _state.update {
            it.copy(
                status = MatchStatus.Finished,
                winner = side
            )
        }
    }

    private fun checkMatchWin() {
        val rightWins = _state.value.teamRight.prevWins
        val leftWins = _state.value.teamLeft.prevWins
        val rightWinCount = rightWins.count { it }
        val leftWinCount = leftWins.count { it }
        val majority: Int = (_state.value.bestOf / 2) + 1

        if (rightWinCount == majority) {
            executeTimelineIntent(MatchIntent.TimelineIntent.WinMatch(Side.TeamRight))
        }
        if (leftWinCount == majority) {
            executeTimelineIntent(MatchIntent.TimelineIntent.WinMatch(Side.TeamLeft))
        }

    }


    private fun undo() {
        var top: MatchIntent.TimelineIntent? = null
        _undoStack.update {
            ArrayDeque(it).apply {
                top = removeLastOrNull()
            }
        }
        if (top != null) {
            _redoStack.update {
                ArrayDeque(it).apply { addLast(top) }
            }
            when (top) {
                is MatchIntent.TimelineIntent.Point -> undoPoint(top)
                is MatchIntent.TimelineIntent.Sub -> undoSub(top)
                is MatchIntent.TimelineIntent.Transfer -> undoTransfer(top)
                is MatchIntent.TimelineIntent.WinGame -> undoWinGame(top)
                is MatchIntent.TimelineIntent.WinMatch -> undoWinMatch()
            }
        }
    }

    private fun undoPoint(intent: MatchIntent.TimelineIntent.Point) {
        _state.update {
            if (intent.side == Side.TeamRight)
                it.copy(teamRight = it.teamRight.copy(currentScore = intent.currentScore))
            else
                it.copy(teamLeft = it.teamLeft.copy(currentScore = intent.currentScore))
        }
    }

    private fun undoTransfer(intent: MatchIntent.TimelineIntent.Transfer) {
        _state.update {
            it.sub(
                from = intent.from.opposite(),
                fromIndex = if (intent.from == Side.TeamRight) it.teamLeft.players.lastIndex else it.teamRight.players.lastIndex,
                toIndex = null
            )
        }
    }

    private fun undoSub(intent: MatchIntent.TimelineIntent.Sub) {
        _state.update {
            it.sub(
                from = intent.from.opposite(),
                fromIndex = intent.indexTo,
                toIndex = intent.indexFrom
            )
        }
    }

    private fun undoWinGame(intent: MatchIntent.TimelineIntent.WinGame) {
        _state.update {
            it.run {
                copy(
                    teamRight = teamRight.copy(
                        prevWins = teamRight.prevWins.dropLast(1),
                        ballPlayer = if (intent.side == Side.TeamRight) intent.ballPlayer else null,
                    ),
                    teamLeft = teamLeft.copy(
                        prevWins = teamLeft.prevWins.dropLast(1),
                        ballPlayer = if (intent.side == Side.TeamLeft) intent.ballPlayer else null,
                    ),
                    ballTeam = ballTeam?.opposite(),
                )
            }
        }.also { undo() }
    }

    private fun undoWinMatch() {
        _state.update {
            it.copy(
                status = MatchStatus.Live,
                winner = null
            )
        }.also { undo() }
    }

    private fun redo() {
        var top: MatchIntent.TimelineIntent? = null
        _redoStack.update {
            ArrayDeque(it).apply {
                top = removeLastOrNull()
            }
        }
        if (top != null)
            executeTimelineIntent(top)
    }
}

