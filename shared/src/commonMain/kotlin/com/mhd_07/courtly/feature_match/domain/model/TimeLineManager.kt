package com.mhd_07.courtly.feature_match.domain.model

import com.mhd_07.courtly.core.domain.model.MatchStatus
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.domain.model.Score
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toPersistentList
import kotlin.time.Clock

class TimeLineManager(
    private val initialState: Match,
    private val reducer: (Match, Event) -> Match
) {
    private val events: MutableList<Event> = mutableListOf()
    private var cursor: Int = -1

    val undoAvailable: Boolean
        get() = cursor >= 0

    val redoAvailable: Boolean
        get() = cursor < events.size - 1

    val currentState: Match
        get() = if (cursor < 0) initialState else events.take(cursor + 1)
            .fold(initialState, reducer).copy(timeLine = events.take(cursor + 1).toPersistentList())

    private val cascadeEvents = setOf(
        Event.Team1Won::class,
        Event.Team2Won::class,
        Event.Team1GameWin::class,
        Event.Team2GameWin::class,
        Event.Team1SetWin::class,
        Event.Team2SetWin::class,
    )

    fun push(event: Event): Match {
        if (redoAvailable) {
            events.subList(cursor + 1, events.size).clear()
        }
        cursor++
        events.add(event)
        return currentState
    }

    fun pointTeam1(player: Player): Match {
        val event = Event.Team1Point(player = player)
        push(event)

        val matchAfterPoint = currentState
        events[cursor] =
            event.copy(snapshot = matchAfterPoint.sets[matchAfterPoint.currentSetIndex].currentGame)

        evaluateWinConditions()
        return currentState
    }

    fun pointTeam2(player: Player): Match {
        val event = Event.Team2Point(player = player)
        push(event)

        val matchAfterPoint = currentState
        events[cursor] =
            event.copy(snapshot = matchAfterPoint.sets[matchAfterPoint.currentSetIndex].currentGame)

        evaluateWinConditions()
        return currentState
    }

    private fun evaluateWinConditions() {
        val match = currentState
        val currentSet = match.sets.getOrNull(match.currentSetIndex) ?: return
        val currentGame = currentSet.currentGame

        // 1. Check Game Win
        if (currentGame.team1Score == Score.Win) {
            winGameTeam1()
        } else if (currentGame.team2Score == Score.Win) {
            winGameTeam2()
        }
    }

    private fun winGameTeam1() {
        val event = Event.Team1GameWin()
        push(event)

        val match = currentState
        val currentSet = match.sets[match.currentSetIndex]
        events[cursor] =
            event.copy(team1Games = currentSet.team1Games, team2Games = currentSet.team2Games)

        checkSetWin(currentSet, match)
    }

    private fun winGameTeam2() {
        val event = Event.Team2GameWin()
        push(event)

        val match = currentState
        val currentSet = match.sets[match.currentSetIndex]
        events[cursor] =
            event.copy(team1Games = currentSet.team1Games, team2Games = currentSet.team2Games)

        checkSetWin(currentSet, match)
    }

    private fun checkSetWin(currentSet: Set, match: Match) {
        val target = match.rules.mode.matchPerSet
        if (currentSet.team1Games == target) {
            winSetTeam1()
        } else if (currentSet.team2Games == target) {
            winSetTeam2()
        }
    }

    private fun winSetTeam1() {
        val event = Event.Team1SetWin()
        push(event)

        val match = currentState
        events[cursor] = event.copy(team1Sets = match.team1Sets, team2Sets = match.team2Sets)

        checkMatchWin(match)
    }

    private fun winSetTeam2() {
        val event = Event.Team2SetWin()
        push(event)

        val match = currentState
        events[cursor] = event.copy(team1Sets = match.team1Sets, team2Sets = match.team2Sets)

        checkMatchWin(match)
    }

    private fun checkMatchWin(match: Match) {
        val target = (match.rules.bestOf / 2) + 1
        if (match.team1Sets == target) {
            push(Event.Team1Won())
        } else if (match.team2Sets == target) {
            push(Event.Team2Won())
        }
    }

    fun start(): Match {
        return push(Event.Start())
    }

    fun undo(): Match {
        if (events[cursor] !is Event.Start)
            if (undoAvailable) {
                // Roll back auto-generated cascade events
                while (cursor >= 0 && events[cursor]::class in cascadeEvents) {
                    cursor--
                }
                // Roll back the user-initiated point event
                if (cursor >= 0) {
                    cursor--
                }
            }
        return currentState
    }

    fun redo(): Match {
        if (redoAvailable) {
            cursor++
            // Replay all associated cascade win events generated alongside this point
            while (redoAvailable && events[cursor + 1]::class in cascadeEvents) {
                cursor++
            }
        }
        return currentState
    }
}