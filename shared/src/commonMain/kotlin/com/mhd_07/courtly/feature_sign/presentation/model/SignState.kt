package com.mhd_07.courtly.feature_sign.presentation.model

import com.mhd_07.courtly.core.presentation.model.RemoteResult

data class SignState(
    val email: String = "",
    val password : String = "",
    val otp : String = "",
    val result : RemoteResult? = null
)
