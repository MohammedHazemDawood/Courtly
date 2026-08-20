package com.mhd_07.courtly.feature_match_setup.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PlayerRequest(
    val id : String,
    val display_name : String,
    val is_remote : Boolean,
)
