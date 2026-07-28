package com.mhd_07.courtly.feature_sign.presentation.module

data class SignState(
    val email: String = "",
    val password : String = "",
    val result : SignResult? = null
)
