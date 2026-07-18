package com.mhd_07.courtly.feature_match_record.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhd_07.courtly.core.domain.model.Match
import com.mhd_07.courtly.core.domain.model.MatchStatus
import com.mhd_07.courtly.core.domain.model.Score
import com.mhd_07.courtly.core.domain.model.Side
import com.mhd_07.courtly.core.domain.model.opposite
import com.mhd_07.courtly.feature_match_record.domain.model.TimelineAction
import com.mhd_07.courtly.feature_match_record.domain.model.TimelineAction.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class MatchRecordViewModel : ViewModel() {
    private val _state = MutableStateFlow(Match.initial)
    val state = _state.asStateFlow()

    private val _undoStack = MutableStateFlow<ArrayDeque<TimelineAction>>(ArrayDeque())
    private val _redoStack = MutableStateFlow<ArrayDeque<TimelineAction>>(ArrayDeque())

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



            is MatchIntent.Point -> {
                if (_state.value.status == MatchStatus.Finished) return //TODO: make it != Live
                val score =
                    if (intent.side == Side.TeamLeft) _state.value.teamLeft.currentScore else _state.value.teamRight.currentScore
                addTimelineIntent(Point(intent.side, score))
                clearRedo()
                executePoint(intent.side)
                checkGameWin()
            }

            is MatchIntent.Sub -> {
                addTimelineIntent(
                    Sub(intent.from, intent.indexFrom, intent.indexTo)
                )
                clearRedo()
                executeTransfer(
                    from = intent.from,
                    indexFrom = intent.indexFrom,
                    indexTo = intent.indexTo
                )
            }

            is MatchIntent.Transfer -> {
                addTimelineIntent(Transfer(intent.from, intent.indexFrom))
                clearRedo()
                executeTransfer(
                    from = intent.from,
                    indexFrom = intent.indexFrom,
                    indexTo = null
                )
            }

            is MatchIntent.EditBestOf -> _state.update { it.copy(bestOf = intent.newBestOf) }
            is MatchIntent.EditBallPlayer -> _state.update {
                if (it.ballTeam == Side.TeamLeft) it.copy(
                    teamLeft = it.teamLeft.copy(ballPlayer = intent.newBallPlayer)
                ) else it.copy(
                    teamRight = it.teamRight.copy(ballPlayer = intent.newBallPlayer)
                )
            }

            is MatchIntent.EditPlayerName -> _state.update {
                if (intent.side == Side.TeamLeft) it.copy(
                    teamLeft = it.teamLeft.copy(
                        players = it.teamLeft.players.toMutableList().apply {
                            set(
                                intent.index,
                                it.teamLeft.players[intent.index].copy(name = intent.newName)
                            )
                        }
                    )
                ) else it.copy(
                    teamRight = it.teamRight.copy(
                        players = it.teamRight.players.toMutableList().apply {
                            set(
                                intent.index,
                                it.teamRight.players[intent.index].copy(name = intent.newName)
                            )
                        }
                    )
                )
            }

            is MatchIntent.EditTeamColor -> _state.update {
                if (intent.side == Side.TeamLeft) it.copy(
                    teamLeft = it.teamLeft.copy(color = intent.newColor)
                ) else it.copy(
                    teamRight = it.teamRight.copy(color = intent.newColor)
                )
            }

            is MatchIntent.EditTeamName -> _state.update {
                if (intent.side == Side.TeamLeft) it.copy(
                    teamLeft = it.teamLeft.copy(name = intent.newName)
                ) else it.copy(
                    teamRight = it.teamRight.copy(name = intent.newName)
                )
            }
            is MatchIntent.StartGame -> _state.update {
                it.copy(
                    status = MatchStatus.Live,
                    ballTeam = intent.startingTeam
                )
            }
        }
    }

    private fun addTimelineIntent(timelineAction: TimelineAction) {
        _undoStack.update {
            ArrayDeque(it).apply { addLast(timelineAction) }
        }
    }

    private fun clearRedo() {
        _redoStack.update { ArrayDeque() }
    }


    private fun executePoint(side: Side) {
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
        val ballPlayer =
            if (side == Side.TeamRight) _state.value.teamRight.ballPlayer else _state.value.teamLeft.ballPlayer
        addTimelineIntent(WinGame(side, ballPlayer = ballPlayer, anotherTeamScore = if (side == Side.TeamRight) _state.value.teamLeft.currentScore else _state.value.teamRight.currentScore))
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
            executeWinGame(Side.TeamRight)
        } else if (leftScore == Score.Win) {
            executeWinGame(Side.TeamLeft)
        }
    }

    private fun executeMatchWin(side: Side) {
        _state.update {
            it.copy(
                status = MatchStatus.Finished,
                winner = side
            )
        }
        addTimelineIntent(WinMatch(side))
    }

    private fun checkMatchWin() {
        val rightWins = _state.value.teamRight.prevWins
        val leftWins = _state.value.teamLeft.prevWins
        val rightWinCount = rightWins.count { it }
        val leftWinCount = leftWins.count { it }
        val majority: Int = (_state.value.bestOf / 2) + 1

        if (rightWinCount == majority) {
            executeMatchWin(Side.TeamRight)
        }
        if (leftWinCount == majority) {
            executeMatchWin(Side.TeamLeft)
        }

    }


    private fun undo() {
        var top: TimelineAction? = null
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
                is Point -> undoPoint(top)
                is Sub -> undoSub(top)
                is Transfer -> undoTransfer(top)
                is WinGame -> undoWinGame(top)
                is WinMatch -> undoWinMatch()
            }
        }
    }

    private fun undoPoint(intent: Point) {
        _state.update {
            if (intent.side == Side.TeamRight)
                it.copy(teamRight = it.teamRight.copy(currentScore = intent.currentScore))
            else
                it.copy(teamLeft = it.teamLeft.copy(currentScore = intent.currentScore))
        }
    }

    private fun undoTransfer(intent: Transfer) {
        _state.update {
            it.sub(
                from = intent.from.opposite(),
                fromIndex = if (intent.from == Side.TeamRight) it.teamLeft.players.lastIndex else it.teamRight.players.lastIndex,
                toIndex = null
            )
        }
    }

    private fun undoSub(intent: Sub) {
        _state.update {
            it.sub(
                from = intent.from.opposite(),
                fromIndex = intent.indexTo,
                toIndex = intent.indexFrom
            )
        }
    }

    private fun undoWinGame(intent: WinGame) {
        _state.update {
            it.run {
                copy(
                    teamRight = teamRight.copy(
                        prevWins = teamRight.prevWins.dropLast(1),
                        ballPlayer = if (intent.side == Side.TeamRight) intent.ballPlayer else null,
                        currentScore = if (intent.side == Side.TeamRight) Score.Zero else intent.anotherTeamScore
                    ),
                    teamLeft = teamLeft.copy(
                        prevWins = teamLeft.prevWins.dropLast(1),
                        ballPlayer = if (intent.side == Side.TeamLeft) intent.ballPlayer else null,
                        currentScore = if (intent.side == Side.TeamLeft) Score.Zero else intent.anotherTeamScore
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
        var top: TimelineAction? = null
        _redoStack.update {
            ArrayDeque(it).apply {
                top = removeLastOrNull()
            }
        }
        top?.let { action ->
            when (action) {
                is Point -> {
                    executePoint(action.side)
                }

                is Sub -> {
                    executeTransfer(action.from, action.indexFrom, action.indexTo)
                }

                is Transfer -> {
                    executeTransfer(action.from, action.indexFrom, null)
                }
                is WinGame -> {
                    _state.update {
                        it.run {
                            copy(
                                teamRight = teamRight.copy(
                                    currentScore = Score.Zero,
                                    prevWins = teamRight.prevWins + (action.side == Side.TeamRight),
                                    ballPlayer = null,
                                ),
                                teamLeft = teamLeft.copy(
                                    currentScore = Score.Zero,
                                    prevWins = teamLeft.prevWins + (action.side == Side.TeamLeft),
                                    ballPlayer = null,
                                ),
                                ballTeam = ballTeam?.opposite(),
                            )
                        }
                    }
                }


                is WinMatch -> {
                    executeMatchWin(action.side)
                }
            }

            addTimelineIntent(action)

            val nextInLine = _redoStack.value.lastOrNull()
            if (nextInLine is WinGame || nextInLine is WinMatch) {
                redo()
            }
        }
    }
}

