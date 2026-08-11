package com.mhd_07.courtly.core.domain.model

import com.mhd_07.courtly.feature_match_record.domain.model.TimelineAction
import kotlin.time.Clock
import kotlin.time.Instant

data class Match(
    val teamLeft: Team = Team(),
    val teamRight: Team = Team(),
    val ballTeam: Side? = null,
    val type: MatchType = MatchType.Double,
    val location: String = "",
    val dateTime: Instant = Clock.System.now(),
    val status: MatchStatus = MatchStatus.Coming,
    val timeline: List<TimelineAction> = emptyList(),
    val searchText : String = "",
    val searchResults : List<Player> = emptyList(),
    val bestOf: Int = 3,
    val winner: Side? = null,
    val mode: MatchMode = MatchMode.Professional,
    val ballHalf: HCourtSide = HCourtSide.Right,
    val ballPlayer: Int? = null,
    val currentScore: Pair<Score, Score> = Score.Zero to Score.Zero,
    val currentSet: Pair<Int, Int> = 0 to 0,
    val prevSets : List<Pair<Int, Int>> = emptyList()
) {
    //TODO: Remove this dummy data
    fun teamRightScore(): Match = when (currentScore.second) {
        Score.Advantage ->
            copy(currentScore = currentScore.copy(second = Score.Win))

        Score.Forty if currentScore.first == Score.Advantage ->
            copy(
                currentScore = Score.Forty to Score.Forty
            )

        Score.Forty if currentScore.first == Score.Forty ->
            copy(currentScore = currentScore.copy(second = Score.Advantage))

        Score.Forty ->
            copy(currentScore = currentScore.copy(second = Score.Win))

        else -> copy(currentScore = currentScore.copy(second = currentScore.second.next()))
    }

    fun teamLeftScore(): Match = when (currentScore.first) {
        Score.Advantage ->
            copy(currentScore = currentScore.copy(first = Score.Win))

        Score.Forty if currentScore.second == Score.Advantage ->
            copy(currentScore = Score.Forty to Score.Forty)

        Score.Forty if currentScore.second == Score.Forty ->
            copy(currentScore = currentScore.copy(first = Score.Advantage))

        Score.Forty ->
            copy(currentScore = currentScore.copy(first = Score.Win))

        else -> copy(currentScore = currentScore.copy(first = currentScore.first.next()))
    }

//    fun teamRightDescore(): Match =
//        copy(teamRight = teamRight.copy(currentScore = teamRight.currentScore.prev()))
//
//    fun teamLeftDescore(): Match =
//        copy(teamLeft = teamLeft.copy(currentScore = teamLeft.currentScore.prev()))

//    fun transfer(index: Int, from: Side): Match {
//        if (index >= (if (from == Side.TeamRight) teamRight else teamLeft).players.size)
//            return this
//        val player =
//            if (from == Side.TeamRight) teamRight.players[index] else teamLeft.players[index]
//        val right =
//            if (from == Side.TeamRight) teamRight.players - player else teamRight.players + player
//        val left =
//            if (from == Side.TeamRight) teamLeft.players + player else teamLeft.players - player
//        return copy(
//            teamRight = teamRight.copy(players = right),
//            teamLeft = teamLeft.copy(players = left)
//        )
//    }

    fun handlePlayers(): Match {
        val right = teamRight.players
        val left = teamLeft.players

        val fieldCap = if (type == MatchType.Double) 2 else 1

        val rightN = right.mapIndexed { index, player ->
            if (index + 1 <= fieldCap)
                player.copy(bench = false)
            else
                player.copy(bench = true)
        }
        val leftN = left.mapIndexed { index, player ->
            if (index + 1 <= fieldCap)
                player.copy(bench = false)
            else
                player.copy(bench = true)
        }

        return copy(
            teamRight = teamRight.copy(players = rightN),
            teamLeft = teamLeft.copy(players = leftN)
        )
    }

    fun sub(from: Side, to: Side, player1: Player, player2: Player?): Match {
        val fromTeam = (if (from == Side.TeamRight) teamRight else teamLeft).players.toMutableList()
        val toTeam = (if (to == Side.TeamRight) teamRight else teamLeft).players.toMutableList()
        val fromIndex = fromTeam.indexOf(player1)
        val toIndex = toTeam.indexOf(player2)

        if (toIndex != -1)
            return this

        return if (from == to) {
            val player1 = fromTeam[fromIndex]
            val player2 = fromTeam[toIndex]
            fromTeam[fromIndex] = player2
            fromTeam[toIndex] = player1
            if (from == Side.TeamRight)
                copy(teamRight = teamRight.copy(players = fromTeam))
            else
                copy(teamLeft = teamLeft.copy(players = fromTeam))
        } else {
            val player1 = fromTeam[fromIndex]
            if (toIndex == -1) {
                fromTeam.removeAt(fromIndex)
                toTeam.add(player1)
            } else {
                val player2 = toTeam[toIndex]
                toTeam[toIndex] = player1
                fromTeam[fromIndex] = player2
            }

            if (from == Side.TeamRight)
                copy(
                    teamRight = teamRight.copy(players = fromTeam),
                    teamLeft = teamLeft.copy(players = toTeam)
                )
            else
                copy(
                    teamLeft = teamLeft.copy(players = fromTeam),
                    teamRight = teamRight.copy(players = toTeam)
                )
        }
    }

}