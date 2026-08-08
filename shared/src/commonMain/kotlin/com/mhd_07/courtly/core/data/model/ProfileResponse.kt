package com.mhd_07.courtly.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val id : String,
    val handle : String?,
    val display_name : String?,
    val avatar_path : String?,
)
