package com.mhd_07.courtly.feature_match_record.domain.model

import com.mhd_07.courtly.core.domain.model.Score
import com.mhd_07.courtly.core.domain.model.Side

sealed interface MatchIntent {

    sealed interface TimelineIntent : MatchIntent {
        data class Point(val side: Side, val currentScore: Score) : TimelineIntent
        data class Transfer(val from: Side, val indexFrom: Int) : TimelineIntent
        data class Sub(val from: Side, val indexFrom: Int, val indexTo: Int) : TimelineIntent
        data class WinGame(val side: Side, val ballPlayer: Int? = null) : TimelineIntent
        data class WinMatch(val side: Side) : TimelineIntent
    }

    object Undo : MatchIntent
    object Redo : MatchIntent
}