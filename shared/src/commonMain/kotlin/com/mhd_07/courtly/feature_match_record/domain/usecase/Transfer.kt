package com.mhd_07.courtly.feature_match_record.domain.usecase

import com.mhd_07.courtly.feature_match_record.domain.model.Match
import com.mhd_07.courtly.feature_match_record.domain.model.Side
import com.mhd_07.courtly.feature_match_record.domain.model.opposite
import kotlin.time.Instant

class Transfer(
    override val time: Instant,
    override val side: Side,
    override val match: Match,
    val index: Int
) : Command {
    override fun execute(): Match = match.sub(
        from = side,
        to = side.opposite(),
        fromIndex = index,
        toIndex = null
    )

    override fun undo(): Match = match.sub(
        from = side.opposite(),
        to = side,
        fromIndex = index,
        toIndex = null
    )
}