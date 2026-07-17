package com.mhd_07.courtly.feature_match_record.data.mapper

import com.mhd_07.courtly.core.domain.model.Score
import com.mhd_07.courtly.feature_match_record.domain.model.TimelineAction
import com.mhd_07.courtly.feature_match_record.presentation.viewmodel.MatchIntent

fun TimelineAction.toIntent() = when (this) {
    is TimelineAction.Point -> MatchIntent.Point(side)
    is TimelineAction.Sub -> MatchIntent.Sub(from, indexFrom, indexTo)
    is TimelineAction.Transfer -> MatchIntent.Transfer(from, indexFrom)
    else -> null
}