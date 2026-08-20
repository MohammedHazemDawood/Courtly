package com.mhd_07.courtly.feature_match_setup.data.model
import com.mhd_07.courtly.core.domain.model.MatchMode
import com.mhd_07.courtly.core.domain.model.MatchStatus
import com.mhd_07.courtly.core.domain.model.MatchType
import com.mhd_07.courtly.feature_match.data.model.RemotePlayer
import com.mhd_07.courtly.feature_match.data.model.RemoteSet
import kotlinx.serialization.Serializable
import kotlin.time.Instant

/*
 * Database mapping (reference):
 * id                -> uuid (string)
 * created_at        -> timestamp with time zone (Instant)
 * host              -> uuid (string)
 * team_left_name    -> text (string)
 * team_right_name   -> text (string)
 * team_left_players -> uuid[] (list of strings)
 * team_right_players-> uuid[] (list of strings)
 * location          -> text (string)
 * type              -> public."MatchType" (string/enum)
 * status            -> public."MatchStatus" (string/enum)
 * mode              -> public."MatchMode" (string/enum)
 * best_of           -> integer (Int)
 * ball_half         -> public."HCourtSide" (string/enum)
 */

@Serializable
data class SetupRequest(
	val created_at: Instant,
	val host: String,
	val team_1_name: String,
	val team_2_name: String,
	val team_1_players: List<PlayerRequest> = emptyList(),
	val team_2_players: List<PlayerRequest> = emptyList(),
	val location: String,
	val type: MatchType,
	val status: MatchStatus,
	val mode: MatchMode,
	val best_of: Int,
	val sets : List<RemoteSet>
//	val ball_half: HCourtSide,
)
