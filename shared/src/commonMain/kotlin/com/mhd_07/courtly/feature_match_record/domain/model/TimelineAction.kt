package com.mhd_07.courtly.feature_match_record.domain.model

import com.mhd_07.courtly.core.domain.model.Score
import com.mhd_07.courtly.core.domain.model.Side

sealed interface TimelineAction {
    data class Point(val side: Side, val currentScore: Score) : TimelineAction
    data class Transfer(val from: Side, val indexFrom: Int) : TimelineAction
    data class Sub(val from: Side, val indexFrom: Int, val indexTo: Int) : TimelineAction
    data class WinGame(val side: Side, val ballPlayer: Int? = null) : TimelineAction
    data class WinMatch(val side: Side) : TimelineAction
}