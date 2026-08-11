package com.mhd_07.courtly.feature_match_record.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhd_07.courtly.core.domain.model.HCourtSide
import com.mhd_07.courtly.core.domain.model.Match
import com.mhd_07.courtly.core.domain.model.MatchStatus
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.domain.model.Score
import com.mhd_07.courtly.core.domain.model.Side
import com.mhd_07.courtly.core.domain.model.opposite
import com.mhd_07.courtly.feature_match_record.domain.model.TimelineAction
import com.mhd_07.courtly.feature_match_record.domain.model.TimelineAction.*
import com.mhd_07.courtly.feature_match_record.domain.usecase.SearchUserUseCase
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class MatchViewModel(
    private val searchUserUseCase: SearchUserUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(Match())
    private val _undoStack = MutableStateFlow<ArrayDeque<TimelineAction>>(ArrayDeque())
    private val _redoStack = MutableStateFlow<ArrayDeque<TimelineAction>>(ArrayDeque())

    private val searchQuery = MutableStateFlow("")

    val state = combine(_state, _undoStack, searchQuery) { state, undoStack, query ->
        state.copy(timeline = undoStack, searchText = query)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _state.value,
    )

//    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            searchQuery
                .debounce(500.milliseconds)
                .collectLatest { query ->

                    val query = query.trim()

                    if (query.isBlank()) {
                        _state.update {
                            it.copy(searchResults = emptyList())
                        }
                        println("None")
                        return@collectLatest
                    }

                    try {
                        val result = searchUserUseCase(query)

                        _state.update {
                            it.copy(searchResults = result)
                        }

                    } catch (e: PostgrestRestException) {
                        _state.update {
                            it.copy(searchResults = emptyList())
                        }
                    }
                }
        }
    }
    override fun onCleared() {
        super.onCleared()
        _undoStack.update { ArrayDeque() }
        _redoStack.update { ArrayDeque() }
        _state.update { Match() }
    }

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
                if (_state.value.status != MatchStatus.Live) return
                addTimelineAction(
                    Point(
                        intent.side,
                        teamLeftScore = _state.value.currentScore.first,
                        teamRightScore = _state.value.currentScore.second
                    )
                )
                clearRedo()
                executePoint(intent.side)
                checkGameWin()
            }

            is MatchIntent.Sub -> {
                addTimelineAction(
                    Sub(intent.side, intent.player1, intent.player2)
                )
                clearRedo()
                executeTransfer(
                    from = intent.side,
                    to = intent.side,
                    player1 = intent.player1,
                    player2 = intent.player2
                )
            }

            is MatchIntent.Transfer -> {
                addTimelineAction(
                    Transfer(
                        from = intent.from,
                        player1 = intent.player1,
                        player2 = intent.player2
                    )
                )
                clearRedo()
                executeTransfer(
                    from = intent.from,
                    to = intent.from.opposite(),
                    player1 = intent.player1,
                    player2 = intent.player2
                )
            }

            is MatchIntent.EditBestOf -> _state.update { it.copy(bestOf = intent.newBestOf) }
            is MatchIntent.EditBallPlayer -> _state.update {
                it.copy(ballPlayer = intent.newBallPlayer)
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
                    ballTeam = intent.startingTeam,
                    ballHalf = HCourtSide.Right
                ).handlePlayers()
            }

            is MatchIntent.EditLocation -> _state.update { it.copy(location = intent.newLocation) }
            is MatchIntent.EditMode -> _state.update { it.copy(mode = intent.mode) }
            is MatchIntent.EditType -> _state.update { it.copy(type = intent.type) }
            is MatchIntent.AddPlayer -> _state.update {
                if (intent.side == Side.TeamLeft)
                    it.copy(teamLeft = it.teamLeft.copy(players = it.teamLeft.players + intent.player))
                else it.copy(teamRight = it.teamRight.copy(players = it.teamRight.players + intent.player))
            }

            is MatchIntent.RemovePlayer -> _state.update {
                if (intent.side == Side.TeamLeft)
                    it.copy(teamLeft = it.teamLeft.copy(players = it.teamLeft.players - intent.player))
                else it.copy(teamRight = it.teamRight.copy(players = it.teamRight.players - intent.player))
            }

            is MatchIntent.SearchPlayers -> searchQuery.update { intent.query }
        }
    }

    private fun addTimelineAction(timelineAction: TimelineAction) {
        _undoStack.update {
            ArrayDeque(it).apply { addLast(timelineAction) }
        }
    }

    private fun clearRedo() {
        _redoStack.update { ArrayDeque() }
    }


    private fun executePoint(side: Side) {
        _state.update { state ->
            val updated =
                if (side == Side.TeamRight)
                    state.teamRightScore()
                else
                    state.teamLeftScore()

            updated.copy(ballHalf = state.ballHalf.opposite())
        }
    }

    private fun executeTransfer(from: Side, to: Side, player1: Player, player2: Player?) {
        _state.update {
            it.sub(
                from = from,
                to = to,
                player1 = player1,
                player2 = player2
            ).handlePlayers()
        }

    }

    private fun executeWinGame(side: Side) {
        val ballPlayer = _state.value.ballPlayer
        val teamLeftScore = _state.value.currentScore.first
        val teamRightScore = _state.value.currentScore.second
        val ballHalf = _state.value.ballHalf
        _state.update {
            it.run {
                copy(
                    currentScore = Score.Zero to Score.Zero,
                    ballPlayer = null,
                    /*teamRight = teamRight.copy(
                        prevWins = teamRight.prevWins + (side == Side.TeamRight),
                    ),
                    teamLeft = teamLeft.copy(
                        prevWins = teamLeft.prevWins + (side == Side.TeamLeft),
                    ),*/
                    ballTeam = ballTeam?.opposite(),
                    ballHalf = HCourtSide.Right,
                    currentSet = currentSet.copy(
                        first = currentSet.first + (if (side == Side.TeamLeft) 1 else 0),
                        second = currentSet.second + (if (side == Side.TeamRight) 1 else 0)
                    )
                )
            }
        }
        addTimelineAction(
            WinGame(
                side,
                ballPlayer = ballPlayer,
                teamLeftScore = teamLeftScore,
                teamRightScore = teamRightScore,
                ballHalf = ballHalf,
                teamLeftWins = _state.value.currentSet.first,
                teamRightWins = _state.value.currentSet.second,
            )
        )
    }

    private fun checkGameWin() {
        val leftScore = _state.value.currentScore.first
        val rightScore = _state.value.currentScore.second

        if (rightScore == Score.Win) {
            executeWinGame(Side.TeamRight)
            checkSetWin()
        } else if (leftScore == Score.Win) {
            executeWinGame(Side.TeamLeft)
            checkSetWin()
        }
    }

    private fun executeMatchWin(side: Side) {
        _state.update {
            it.copy(
                status = MatchStatus.Finished,
                winner = side
            )
        }
        addTimelineAction(WinMatch(side))
    }

    private fun checkMatchWin() {
        val leftWinsCount = _state.value.prevSets.count { pair -> pair.first > pair.second }
        val rightWinsCount = _state.value.prevSets.count { pair -> pair.second > pair.first }

        val majority: Int = (_state.value.bestOf / 2) + 1

        if (rightWinsCount == majority) {
            executeMatchWin(Side.TeamRight)
        }
        if (leftWinsCount == majority) {
            executeMatchWin(Side.TeamLeft)
        }
    }

    private fun checkSetWin() {
        if (_state.value.currentSet.first == _state.value.mode.matchPerSet) {
            executeSetWin(Side.TeamLeft)
            checkMatchWin()
        } else if (_state.value.currentSet.second == _state.value.mode.matchPerSet) {
            executeSetWin(Side.TeamRight)
            checkMatchWin()
        }
    }

    private fun executeSetWin(side: Side) {
        addTimelineAction(WinSet(side = side, result = _state.value.currentSet))
        _state.update {
            it.copy(
//                teamLeft = it.teamLeft.copy(prevWins = it.teamLeft.prevWins + (side == Side.TeamLeft)),
//                teamRight = it.teamRight.copy(prevWins = it.teamRight.prevWins + (side == Side.TeamRight)),
                prevSets = it.prevSets + (it.currentSet.first to it.currentSet.second),
                currentSet = 0 to 0
            )
        }
//        checkMatchWin()
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
                is WinSet -> undoWinSet(top)
            }
        }
    }

    private fun undoPoint(intent: Point) {
        _state.update {
            it.copy(
                currentScore = intent.teamLeftScore to intent.teamRightScore,
                ballHalf = it.ballHalf.opposite()
            )
        }
    }

    private fun undoTransfer(intent: Transfer) {
        _state.update {
            it.sub(
                from = intent.from.opposite(),
                to = intent.from,
                player1 = intent.player1,
                player2 = intent.player2
            ).handlePlayers()
        }
    }

    private fun undoSub(intent: Sub) {
        _state.update {
            it.sub(
                from = intent.side,
                to = intent.side,
                player1 = intent.player1,
                player2 = intent.player2
            ).handlePlayers()
        }
    }

    private fun undoWinGame(intent: WinGame) {
        _state.update {
            it.run {
                copy(
                    ballPlayer = intent.ballPlayer,
                    currentScore = intent.teamLeftScore to intent.teamRightScore,
//                    teamRight = teamRight.copy(
//                        prevWins = teamRight.prevWins.dropLast(1)
//                    ),
//                    teamLeft = teamLeft.copy(
//                        prevWins = teamLeft.prevWins.dropLast(1),
//                    ),
                    ballTeam = ballTeam?.opposite(),
                    ballHalf = intent.ballHalf,
                    currentSet = currentSet.copy(
                        first = (currentSet.first - (if (intent.side == Side.TeamLeft) 1 else 0)).coerceAtLeast(
                            0
                        ),
                        second = (currentSet.second - (if (intent.side == Side.TeamRight) 1 else 0)).coerceAtLeast(
                            0
                        )
                    )
                )
            }
        }
        undo()
    }

    private fun undoWinMatch() {
        _state.update {
            it.copy(
                status = MatchStatus.Live,
                winner = null
            )
        }
        undo()
    }

    private fun undoWinSet(action: WinSet) {
        _state.update {
            it.run {
                copy(
//                    teamLeft = teamLeft.copy(prevWins = teamLeft.prevWins.dropLast(1)),
//                    teamRight = teamRight.copy(prevWins = teamRight.prevWins.dropLast(1)),
                    prevSets = prevSets.dropLast(1),
                    currentSet = action.result
                )
            }
        }
        undo()
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
                    addTimelineAction(
                        Point(
                            action.side,
                            teamLeftScore = state.value.currentScore.first,
                            teamRightScore = state.value.currentScore.second
                        )
                    )
                    executePoint(action.side)
                }

                is Sub -> {
                    executeTransfer(action.side, action.side, action.player1, action.player2)
                    addTimelineAction(
                        Sub(
                            side = action.side,
                            player1 = action.player1,
                            player2 = action.player2
                        )
                    )
                }

                is Transfer -> {
                    executeTransfer(
                        action.from,
                        action.from.opposite(),
                        action.player1,
                        action.player2
                    )
                    addTimelineAction(
                        Transfer(
                            from = action.from,
                            player1 = action.player1,
                            player2 = action.player2
                        )
                    )
                }

                is WinGame -> {
                    executeWinGame(side = action.side)
                    /*_state.update {
                        it.run {
                            copy(
                                ballPlayer = null,
                                currentScore = Score.Zero to Score.Zero,
                                teamRight = teamRight.copy(
                                    prevWins = teamRight.prevWins + (action.side == Side.TeamRight),
                                ),
                                teamLeft = teamLeft.copy(
                                    prevWins = teamLeft.prevWins + (action.side == Side.TeamLeft),
                                ),
                                ballTeam = ballTeam?.opposite(),
                                currentSet = currentSet.copy(
                                    first = currentSet.first + (if (action.side == Side.TeamLeft) 1 else 0),
                                    second = currentSet.second + (if (action.side == Side.TeamRight) 1 else 0)
                                )
                            )
                        }
                    }*/
                }


                is WinMatch -> {
                    executeMatchWin(action.side)
                }

                is WinSet -> executeSetWin(action.side)
            }

//            addTimelineAction(action)

            val nextInLine = _redoStack.value.lastOrNull()
            if (nextInLine is WinGame || nextInLine is WinMatch || nextInLine is WinSet) {
                redo()
            }
        }
    }
}

