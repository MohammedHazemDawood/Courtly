package com.mhd_07.courtly.core.domain.model

enum class HCourtSide {
    Right,
    Left
}

fun HCourtSide.opposite(): HCourtSide =
    if (this == HCourtSide.Right) HCourtSide.Left else HCourtSide.Right