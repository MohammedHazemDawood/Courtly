package com.mhd_07.courtly.feature_match_setup.domain.model

import com.mhd_07.courtly.core.domain.model.HCourtSide
import com.mhd_07.courtly.core.domain.model.MatchMode
import com.mhd_07.courtly.core.domain.model.MatchStatus
import com.mhd_07.courtly.core.domain.model.MatchType
import com.mhd_07.courtly.core.domain.model.Team
import kotlin.time.Clock
import kotlin.time.Instant

data class Setup(
    val teamLeft: Team = Team(),

    val teamRight: Team = Team(),

//    val host : Player,

    val type: MatchType = MatchType.Double,

    val location: String = "",

    val createdAt: Instant = Clock.System.now(),

    val status: MatchStatus = MatchStatus.Coming,

    val bestOf: Int = 3,

    val mode: MatchMode = MatchMode.Professional,

    val ballHalf: HCourtSide = HCourtSide.Right

)
