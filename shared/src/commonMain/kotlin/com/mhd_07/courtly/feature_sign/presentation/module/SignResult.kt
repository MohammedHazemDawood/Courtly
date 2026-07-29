package com.mhd_07.courtly.feature_sign.presentation.module

import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.app_name
import io.github.jan.supabase.auth.exception.AuthErrorCode
import org.jetbrains.compose.resources.StringResource

sealed interface SignResult {
    data object Loading : SignResult
    data object Success/*(val acc : Account)*/ : SignResult

    //    data object Ideal : SignResult
    data class Error(val error: SignError) : SignResult
}

enum class SignError(val message: StringResource) {
    AccountExist(message = Res.string.app_name),
    InvalidCredentials(message = Res.string.app_name),
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
    AuthErrorCode.EmailNotConfirmed to SignError.NotConfirmed,
    AuthErrorCode.InvalidCredentials to SignError.InvalidCredentials,
    AuthErrorCode.EmailExists to SignError.AccountExist,
    AuthErrorCode.WeakPassword to SignError.WeakPassword,
    AuthErrorCode.UserBanned to SignError.UserPanned,
    AuthErrorCode.SamePassword to SignError.SamePassword,
    AuthErrorCode.OverRequestRateLimit to SignError.TooManyRequest,

)

fun getError(error: AuthErrorCode?) = messages[error] ?: SignError.Unknown