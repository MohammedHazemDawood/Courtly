package com.mhd_07.courtly.feature_profile.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CheckHandleResponse(
    val available : Boolean,
//    val reason : String
)
