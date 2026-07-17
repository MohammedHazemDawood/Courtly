package com.mhd_07.courtly.feature_match_record.presentation.viewmodel

import com.mhd_07.courtly.core.domain.model.Side

sealed interface MatchIntent {

    data class Point(val side: Side) : MatchIntent
    data class Transfer(val from: Side, val indexFrom: Int) : MatchIntent
    data class Sub(val from: Side, val indexFrom: Int, val indexTo: Int) : MatchIntent

    object Undo : MatchIntent
    object Redo : MatchIntent
}