package com.mhd_07.courtly.feature_sign.presentation.module

sealed interface SignIntent {
    data object Sign : SignIntent
//    object Logout : SignIntent

    data class EditEmail(val email: String) : SignIntent
    data class EditPassword(val password: String) : SignIntent
    data class EditOTP(val otp: String) : SignIntent
    data object ResendOTP : SignIntent
    data object VerifyOTP : SignIntent
}