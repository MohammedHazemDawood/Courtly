package com.mhd_07.courtly.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class MatchStatus(val display: String) {
    Coming("Upcoming"),
    Live("Live"),
    Finished("Finished")

}