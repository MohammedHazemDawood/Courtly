package com.mhd_07.courtly.feature_match.domain.model

import com.mhd_07.courtly.core.domain.model.HCourtSide
import com.mhd_07.courtly.core.domain.model.MatchStatus
import com.mhd_07.courtly.core.domain.model.MatchType
import com.mhd_07.courtly.core.domain.model.Score
import com.mhd_07.courtly.core.domain.model.Side
import com.mhd_07.courtly.core.domain.model.next
import com.mhd_07.courtly.core.domain.model.opposite
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.Clock
import kotlin.time.Instant

data class Match(
    val id: String,
    val createdAt: Instant,
    val startedAt: Instant?,
    val doneAt: Instant?,
    val hostId: String,
    val team1: Team,
    val team2: Team,
    val team1Sets: Int,
    val team2Sets: Int,
    val sets: PersistentList<Set>,
    val rules: Rules,
    val winner: Side?,
    val status: MatchStatus,
    val currentSetIndex: Int,
    val currentServeSide: Side,
    val currentCourtSide: HCourtSide,
    val timeLine: PersistentList<Event>,
) {
    companion object {
        val initial = Match(
            id = "",
            createdAt = Clock.System.now(),
            startedAt = null,
            doneAt = null,
            hostId = "",
            team1 = Team(),
            team2 = Team(),
            team1Sets = 0,
            team2Sets = 0,
            sets = persistentListOf(Set()),
            rules = Rules(),
            winner = null,
            status = MatchStatus.Coming,
            currentSetIndex = 0,
            currentServeSide = Side.Team1,
            currentCourtSide = HCourtSide.Left,
            timeLine = persistentListOf()
        )
    }

    fun nextPoint(target: Score, another: Score): Pair<Score, Score> = when (target) {
        Score.Forty if another == Score.Advantage -> Score.Forty to Score.Forty
        Score.Forty if another == Score.Forty -> Score.Advantage to Score.Forty
        Score.Forty -> Score.Win to another
        else -> target.next() to another
    }

    fun pointTeam1(): Match {
        if (status != MatchStatus.Live)
            return this
        return sets[currentSetIndex].let { set ->
            val (team1Score, team2Score) = nextPoint(
                set.currentGame.team1Score,
                set.currentGame.team2Score
            )
            copy(
                sets = sets.replacingAt(
                    currentSetIndex, set.copy(
                        currentGame = set.currentGame.copy(
                            team1Score = team1Score,
                            team2Score = team2Score
                        )
                    )
                ),
                currentCourtSide = currentCourtSide.opposite()

            )
        }
    }

    fun pointTeam2(): Match {
        if (status != MatchStatus.Live)
            return this
        return sets[currentSetIndex].let { set ->
            val (team2Score, team1Score) = nextPoint(
                set.currentGame.team2Score,
                set.currentGame.team1Score
            )
            copy(
                sets = sets.replacingAt(
                    currentSetIndex, set.copy(
                        currentGame = set.currentGame.copy(
                            team1Score = team1Score,
                            team2Score = team2Score
                        )
                    )
                ),
                currentCourtSide = currentCourtSide.opposite()
            )
        }
    }

    fun winGameTeam1(): Match {
        return sets[currentSetIndex].let { set ->
            copy(
                sets = sets.replacingAt(
                    currentSetIndex, set.copy(
                        team1Games = set.team1Games + 1,
                        games = set.games.adding(set.currentGame),
                        currentGame = Game(Score.Zero, Score.Zero)
                    )
                ), currentServeSide = currentServeSide.opposite()
            )
        }
    }

    fun winGameTeam2(): Match {
        return sets[currentSetIndex].let { set ->
            copy(
                sets = sets.replacingAt(
                    currentSetIndex, set.copy(
                        team2Games = set.team2Games + 1,
                        games = set.games.adding(set.currentGame),
                        currentGame = Game(Score.Zero, Score.Zero)
                    )
                ), currentServeSide = currentServeSide.opposite()
            )
        }
    }

    fun winSetTeam1(): Match {
        return copy(team1Sets = team1Sets + 1).let {
            if (sets.size != rules.bestOf)
                it.copy(currentSetIndex = currentSetIndex + 1, sets = sets.adding(Set()))
            else it
        }
    }

    fun winSetTeam2(): Match {
        return copy(team2Sets = team2Sets + 1).let {
            if (sets.size != rules.bestOf)
                it.copy(currentSetIndex = currentSetIndex + 1, sets = sets.adding(Set()))
            else it
        }
    }

    fun winMatchTeam1(): Match {
        return copy(winner = Side.Team1)
    }

    fun winMatchTeam2(): Match {
        return copy(winner = Side.Team2)
    }

    fun sortPlayers(): Match {
        val type = if(team1.players.size < 2) MatchType.Single else rules.type
        return copy(
            team1 = team1.updateBenchStatus(2),
            team2 = team2.updateBenchStatus(2),
            rules = rules.copy(type = type)
        )
    }

    private fun Team.updateBenchStatus(activePlayersLimit: Int): Team {
        val updatedPlayers = players.mapIndexed { index, player ->
            val isBench = index >= activePlayersLimit
            if (player.bench != isBench) {
                player.copy(bench = isBench)
            } else {
                player
            }
        }
        return copy(players = updatedPlayers)
    }
}