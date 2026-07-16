package com.mhd_07.courtly.feature_match_record.domain.model

enum class Side {
    TeamRight,
    TeamLeft
}

fun Side.opposite(): Side = if (this == Side.TeamRight) Side.TeamLeft else Side.TeamRight