package com.mhd_07.courtly.feature_match_record.domain.usecase

import com.mhd_07.courtly.feature_match_record.domain.model.Match
import com.mhd_07.courtly.feature_match_record.domain.model.Side
import kotlin.time.Instant

interface Command {
    val time: Instant
    val side: Side
    val match: Match

    fun execute(): Match
    fun undo(): Match
}