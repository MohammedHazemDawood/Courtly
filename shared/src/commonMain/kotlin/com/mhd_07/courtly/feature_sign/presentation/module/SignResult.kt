package com.mhd_07.courtly.feature_sign.presentation.module

import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.app_name
import org.jetbrains.compose.resources.StringResource

sealed interface SignResult {
    data object Loading : SignResult
    data object Success/*(val acc : Account)*/ : SignResult

    //    data object Ideal : SignResult
    data class Error(val error: SignError) : SignResult
}

enum class SignError(val message: StringResource) {
    AccountExist(message = Res.string.app_name),
    NotConfirmed(message = Res.string.app_name),
    TooManyRequest(message = Res.string.app_name),
    SamePassword(message = Res.string.app_name),
    UserPanned(message = Res.string.app_name),
    WeakPassword(message = Res.string.app_name),
    NetworkError(message = Res.string.app_name),
    Canceled(message = Res.string.app_name),
    Unknown(message = Res.string.app_name)
}

private val messages = mapOf(
    //TODO: To be change all string res
    "email_exists" to SignError.AccountExist,
    "email_not_confirmed" to SignError.NotConfirmed,
    "over_email_send_rate_limit" to SignError.TooManyRequest,
    "over_request_rate_limit" to SignError.TooManyRequest,
    "same_password" to SignError.SamePassword,
    "user_banned" to SignError.UserPanned,
    "weak_password" to SignError.WeakPassword,
)

fun getError(error: String) = messages[error] ?: SignError.Unknown