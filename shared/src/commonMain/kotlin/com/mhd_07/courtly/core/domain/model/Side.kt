package com.mhd_07.courtly.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class Side {
    TeamRight,
    TeamLeft
}

fun Side.opposite(): Side = if (this == Side.TeamRight) Side.TeamLeft else Side.TeamRight