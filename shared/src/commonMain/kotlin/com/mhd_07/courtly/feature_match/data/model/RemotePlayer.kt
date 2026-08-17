package com.mhd_07.courtly.feature_match.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RemotePlayer(
    val id : String,
    val handle : String?,
    val name : String,
    val avatar : String?,
    val avatar_version : Int = 0,
    val bench : Boolean = false,
    val is_remote : Boolean = true
)
