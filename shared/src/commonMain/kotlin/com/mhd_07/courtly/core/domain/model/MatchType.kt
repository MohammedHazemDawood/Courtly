package com.mhd_07.courtly.core.domain.model

import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.double
import courtly.shared.generated.resources.single
import org.jetbrains.compose.resources.StringResource

enum class MatchType(val display : StringResource) {
    Single(Res.string.single),
    Double(Res.string.double)
}