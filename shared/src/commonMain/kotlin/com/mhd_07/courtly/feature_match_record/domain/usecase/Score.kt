package com.mhd_07.courtly.feature_match_record.domain.usecase

import com.mhd_07.courtly.feature_match_record.domain.model.Match
import com.mhd_07.courtly.feature_match_record.domain.model.Side
import kotlin.time.Instant

class Score(
    override val time: Instant,
    override val side: Side,
    override val match: Match
) : Command {
    override fun execute(): Match =
        if (side == Side.TeamRight) match.teamRightScore() else match.teamLeftScore()

    override fun undo(): Match =
        if (side == Side.TeamRight) match.teamLeftDescore() else match.teamRightDescore()
}