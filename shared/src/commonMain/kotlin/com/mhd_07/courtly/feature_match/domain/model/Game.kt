package com.mhd_07.courtly.feature_match.domain.model

import com.mhd_07.courtly.core.domain.model.Score
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Game(
    @EncodeDefault(EncodeDefault.Mode.ALWAYS)
    @SerialName("team_1_score")
    val team1Score: Score = Score.Zero,
    @EncodeDefault(EncodeDefault.Mode.ALWAYS)
    @SerialName("team_2_score")
    val team2Score: Score = Score.Zero,
)
