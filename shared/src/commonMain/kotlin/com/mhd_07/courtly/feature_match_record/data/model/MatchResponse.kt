package com.mhd_07.courtly.feature_match_record.data.model

import com.mhd_07.courtly.core.domain.model.HCourtSide
import com.mhd_07.courtly.core.domain.model.MatchMode
import com.mhd_07.courtly.core.domain.model.MatchStatus
import com.mhd_07.courtly.core.domain.model.MatchType
import com.mhd_07.courtly.core.domain.model.Score
import com.mhd_07.courtly.core.domain.model.Side
import com.mhd_07.courtly.feature_match_record.domain.model.TimelineAction
import kotlinx.serialization.Serializable

/**
string
id
uuid

string
created_at
timestamp with time zone

string
host
uuid

string
team_left_name
text

string
team_right_name
text

string
team_left_players
uuid[]

array
team_right_players
uuid[]

array
location
text

string
type
public."MatchType"

string
status
public."MatchStatus"

string
mode
public."MatchMode"

string
best_of
integer

number
winner
public."Side"

string
ball_half
public."HCourtSide"

string
team_left_current_score
public."Score"

string
team_right_current_score
public."Score"

string
team_left_current_set
integer

number
team_right_current_set
integer

number
team_left_prev_sets
integer[]

array
ball_player
uuid

string
ball_team
public."Side"

string
team_right_prev_sets
integer[]

array
timeline
jsonb[]

array
 */

@Serializable
data class MatchResponse(
    val id: String,
    val created_at: String,
    val host: String,
    val team_left_name: String ,
    val team_right_name: String,
    val team_left_players: List<String> = emptyList(),
    val team_right_players: List<String> = emptyList(),
    val location: String? = null,
    val type: MatchType,
    val status: MatchStatus,
    val mode: MatchMode,
    val best_of: Int = 3,
    val winner: Side? = null,
    val ball_half: HCourtSide = HCourtSide.Right,
    val team_left_current_score: Score = Score.Zero,
    val team_right_current_score: Score = Score.Zero,
    val team_left_current_set: Int = 0,
    val team_right_current_set: Int = 0,
    val team_left_prev_sets: List<Int> = emptyList(),
    val team_right_prev_sets: List<Int> = emptyList(),
    val ball_player: String? = null,
    val ball_team: Side? = null,
    val timeline: List<TimelineAction> = emptyList()
)