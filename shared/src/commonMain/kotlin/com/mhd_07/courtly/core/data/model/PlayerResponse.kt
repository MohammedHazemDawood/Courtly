package com.mhd_07.courtly.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PlayerResponse(
    val id : String,
    val handle : String?,
    val display_name : String?,
    val avatar_path : String?,
    val bio : String?,
    val avatar_version : Int,
)
