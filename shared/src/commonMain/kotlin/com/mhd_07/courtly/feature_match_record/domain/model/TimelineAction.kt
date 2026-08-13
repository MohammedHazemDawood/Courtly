package com.mhd_07.courtly.feature_match_record.domain.model

import com.mhd_07.courtly.core.domain.model.HCourtSide
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.domain.model.Score
import com.mhd_07.courtly.core.domain.model.Side
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.Instant

@Serializable
sealed interface TimelineAction {
    val time: Instant

    @Serializable
    data class Point(
        val side: Side,
        val teamRightScore: Score,
        val teamLeftScore: Score,
        override val time: Instant = Clock.System.now()
    ) :
        TimelineAction

    @Serializable
    data class Transfer(
        val from: Side,
        val player1: Player,
        val player2: Player?,
        override val time: Instant = Clock.System.now()
    ) : TimelineAction

    @Serializable
    data class Sub(
        val side: Side, val player1: Player, val player2: Player,
        override val time: Instant = Clock.System.now()
    ) : TimelineAction

    @Serializable
    data class WinGame(
        val side: Side,
        val teamRightScore: Score,
        val teamRightWins: Int,
        val teamLeftScore: Score,
        val teamLeftWins: Int,
        val ballPlayer: Int? = null,
        val ballHalf: HCourtSide,
        override val time: Instant = Clock.System.now()
    ) : TimelineAction

    @Serializable
    data class WinMatch(val side: Side, override val time: Instant = Clock.System.now()) :
        TimelineAction

    @Serializable
    data class WinSet(
        override val time: Instant = Clock.System.now(),
        val side: Side,
        val result: Pair<Int, Int>
    ) : TimelineAction
}