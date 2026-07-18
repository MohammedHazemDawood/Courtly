package com.mhd_07.courtly.feature_match_record.domain.model

import com.mhd_07.courtly.core.domain.model.HCourtSide
import com.mhd_07.courtly.core.domain.model.Score
import com.mhd_07.courtly.core.domain.model.Side
import kotlin.time.Clock
import kotlin.time.Instant

sealed interface TimelineAction {
    val time: Instant

    data class Point(
        val side: Side,
        val teamRightScore: Score,
        val teamLeftScore: Score,
        override val time: Instant = Clock.System.now()
    ) :
        TimelineAction

    data class Transfer(
        val from: Side,
        val indexFrom: Int,
        val indexTo: Int?,
        override val time: Instant = Clock.System.now()
    ) :
        TimelineAction

    data class Sub(
        val side: Side, val indexFrom: Int, val indexTo: Int,
        override val time: Instant = Clock.System.now()
    ) : TimelineAction

    data class WinGame(
        val side: Side,
        val teamRightScore: Score,
        val teamRightWins: Int ,
        val teamLeftScore: Score,
        val teamLeftWins: Int,
        val ballPlayer: Int? = null,
        val ballHalf: HCourtSide,
        override val time: Instant = Clock.System.now()
    ) : TimelineAction

    data class WinMatch(val side: Side, override val time: Instant = Clock.System.now()) :
        TimelineAction
}