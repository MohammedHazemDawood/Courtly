package com.mhd_07.courtly.core.domain.model

import com.mhd_07.courtly.feature_match_record.domain.model.TimelineAction
import kotlin.time.Clock
import kotlin.time.Instant

data class Match(
    val teamLeft: Team,
    val teamRight: Team,
    val ballTeam: Side? = null,
    val type: MatchType,
    val location: String,
    val dateTime: Instant = Clock.System.now(),
    val status: MatchStatus,
    val timeline: List<TimelineAction>,
    val bestOf: Int = 3,
    val winner: Side? = null,
    val ballHalf : HCourtSide = HCourtSide.Right
) {
    companion object {
        val initial = Match(
            teamLeft = Team.initial,
            teamRight = Team.initial,
            type = MatchType.Single,
            location = "",
            status = MatchStatus.Coming,
            timeline = emptyList()
        )
    }

    fun teamRightScore(): Match = when {
        teamRight.currentScore == Score.Advantage ->
            copy(teamRight = teamRight.copy(currentScore = Score.Win))
        teamRight.currentScore == Score.Forty &&
                teamLeft.currentScore == Score.Advantage ->
            copy(
                teamRight = teamRight.copy(currentScore = Score.Forty),
                teamLeft = teamLeft.copy(currentScore = Score.Forty))
        teamRight.currentScore == Score.Forty &&
                teamLeft.currentScore == Score.Forty ->
            copy(teamRight = teamRight.copy(currentScore = Score.Advantage))
        teamRight.currentScore == Score.Forty ->
            copy(teamRight = teamRight.copy(currentScore = Score.Win))
        else ->
            copy(teamRight = teamRight.copy(currentScore = teamRight.currentScore.next()))
    }

    fun teamLeftScore(): Match = when {
        teamLeft.currentScore == Score.Advantage ->
            copy(teamLeft = teamLeft.copy(currentScore = Score.Win))

        teamLeft.currentScore == Score.Forty &&
                teamRight.currentScore == Score.Advantage ->
            copy(
                teamLeft = teamLeft.copy(currentScore = Score.Forty),
                teamRight = teamRight.copy(currentScore = Score.Forty)
            )

        teamLeft.currentScore == Score.Forty &&
                teamRight.currentScore == Score.Forty ->
            copy(teamLeft = teamLeft.copy(currentScore = Score.Advantage))

        teamLeft.currentScore == Score.Forty ->
            copy(teamLeft = teamLeft.copy(currentScore = Score.Win))

        else ->
            copy(teamLeft = teamLeft.copy(currentScore = teamLeft.currentScore.next()))
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

    fun sub(from: Side, fromIndex: Int, toIndex: Int?): Match {
        val to = from.opposite()
        val fromTeam = (if (from == Side.TeamRight) teamRight else teamLeft).players.toMutableList()
        val toTeam = (if (to == Side.TeamRight) teamRight else teamLeft).players.toMutableList()

        if (fromIndex >= fromTeam.size)
            return this
        if (toIndex != null && toIndex >= toTeam.size)
            return this

        return if (from == to) {
            val player1 = fromTeam[fromIndex]
            val player2 = fromTeam[toIndex!!]
            fromTeam[fromIndex] = player2
            fromTeam[toIndex] = player1
            if (from == Side.TeamRight)
                copy(teamRight = teamRight.copy(players = fromTeam))
            else
                copy(teamLeft = teamLeft.copy(players = fromTeam))
        } else {
            val player1 = fromTeam[fromIndex]
            if (toIndex == null) {
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