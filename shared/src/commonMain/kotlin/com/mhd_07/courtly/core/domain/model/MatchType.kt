package com.mhd_07.courtly.core.domain.model

import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.double
import courtly.shared.generated.resources.single
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

@Serializable
enum class MatchType(val display : StringResource) {
    Single(Res.string.single),
    Double(Res.string.double)
}