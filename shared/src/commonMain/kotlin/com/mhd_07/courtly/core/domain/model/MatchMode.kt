package com.mhd_07.courtly.core.domain.model

import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.mode_freestyle
import courtly.shared.generated.resources.mode_professional
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource


@Serializable
enum class MatchMode(val display : StringResource, val matchPerSet : Int) {
    Professional(Res.string.mode_professional, 6),
    FreeStyle(Res.string.mode_freestyle, 1)
}