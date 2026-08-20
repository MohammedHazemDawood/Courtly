package com.mhd_07.courtly.feature_match.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RemotePlayer(
    val id : String,
    val handle : String? = null,
    val display_name : String,
    val avatar_path : String? = null,
    val avatar_version : Int = 0,
    val bench : Boolean = false,
    val is_remote : Boolean = true
)
