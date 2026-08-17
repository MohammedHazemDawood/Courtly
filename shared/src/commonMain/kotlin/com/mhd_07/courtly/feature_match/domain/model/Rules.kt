package com.mhd_07.courtly.feature_match.domain.model

import com.mhd_07.courtly.core.domain.model.MatchMode
import com.mhd_07.courtly.core.domain.model.MatchType

data class Rules(
    val bestOf: Int = 3,
    val type: MatchType = MatchType.Double,
    val mode: MatchMode = MatchMode.Professional
)