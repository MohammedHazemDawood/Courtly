package com.mhd_07.courtly.feature_match.data.model

import com.mhd_07.courtly.core.domain.model.HCourtSide
import com.mhd_07.courtly.core.domain.model.MatchMode
import com.mhd_07.courtly.core.domain.model.MatchStatus
import com.mhd_07.courtly.core.domain.model.MatchType
import com.mhd_07.courtly.core.domain.model.Side
import com.mhd_07.courtly.feature_match.domain.model.Event
import com.mhd_07.courtly.feature_match.domain.model.Set
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class RemoteMatch(
    val id: String,
    val created_at: Instant,
    val started_at: Instant?,
    val host: String,
    val team_1_name: String,
    val team_2_name: String,
    val team_1_players: List<RemotePlayer>, // Represented as JSON strings or custom objects
    val team_2_players: List<RemotePlayer>,
    val team_1_sets: Int,
    val team_2_sets: Int,
    val current_set_index: Int,
    val current_serve_side: Side,
    val current_court_side: HCourtSide,
    val status: MatchStatus,
    val winner: Side?,
    val type: MatchType,
    val mode: MatchMode,
    val best_of: Int,
    val sets: List<RemoteSet>,
    val timeline: List<Event>,
    val done_at: Instant?
)

