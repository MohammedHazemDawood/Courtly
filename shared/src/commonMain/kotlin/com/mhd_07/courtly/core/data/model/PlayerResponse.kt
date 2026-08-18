package com.mhd_07.courtly.core.data.model

import com.mhd_07.courtly.core.domain.model.Visibility
import kotlinx.serialization.Serializable

@Serializable
data class PlayerResponse(
    val id : String,
    val handle : String?,
    val display_name : String?,
    val avatar_path : String?,
    val cover : String?,
    val cover_v : Int,
    val bio : String?,
    val avatar_version : Int,
    val visibility: Visibility,
    val location : String?
)
