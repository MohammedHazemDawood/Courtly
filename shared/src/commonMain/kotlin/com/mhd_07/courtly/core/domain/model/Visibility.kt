package com.mhd_07.courtly.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class Visibility {
    Public,
    Protected,
    Private
}