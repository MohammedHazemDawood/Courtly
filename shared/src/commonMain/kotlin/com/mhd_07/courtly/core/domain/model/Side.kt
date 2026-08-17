package com.mhd_07.courtly.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class Side {
    Team1,
    Team2
}

fun Side.opposite(): Side = if (this == Side.Team2) Side.Team1 else Side.Team2