package com.mhd_07.courtly.core.domain.model

enum class Side {
    TeamRight,
    TeamLeft
}

fun Side.opposite(): Side = if (this == Side.TeamRight) Side.TeamLeft else Side.TeamRight