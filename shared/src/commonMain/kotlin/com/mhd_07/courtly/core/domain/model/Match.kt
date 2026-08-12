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
    val bestOf: Int = 3,
    val winner: Side? = null,
    val mode: MatchMode = MatchMode.Professional,
    val ballHalf: HCourtSide = HCourtSide.Right,
    val ballPlayer: Int? = null,
    val currentScore: Pair<Score, Score> = Score.Zero to Score.Zero,
    val currentSet: Pair<Int, Int> = 0 to 0,
    val prevSets : List<Pair<Int, Int>> = emptyList()
)