package com.mhd_07.courtly.feature_sign.presentation.module

import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.account_exist
import courtly.shared.generated.resources.app_name
import courtly.shared.generated.resources.canceled
import courtly.shared.generated.resources.invalid_credentials
import courtly.shared.generated.resources.network_error
import courtly.shared.generated.resources.not_confirmed
import courtly.shared.generated.resources.same_password
import courtly.shared.generated.resources.too_many_request
import courtly.shared.generated.resources.unknown
import courtly.shared.generated.resources.user_banned
import courtly.shared.generated.resources.weak_password
import io.github.jan.supabase.auth.exception.AuthErrorCode
import org.jetbrains.compose.resources.StringResource

sealed interface SignResult {
    data object Loading : SignResult
    data object Success/*(val acc : Account)*/ : SignResult

    //    data object Ideal : SignResult
    data class Error(val error: SignError) : SignResult
}

enum class SignError(val message: StringResource) {
    AccountExist(message = Res.string.account_exist),
    InvalidCredentials(message = Res.string.invalid_credentials),
    NotConfirmed(message = Res.string.not_confirmed),
    TooManyRequest(message = Res.string.too_many_request),
    SamePassword(message = Res.string.same_password),
    UserPanned(message = Res.string.user_banned),
    WeakPassword(message = Res.string.weak_password),
    NetworkError(message = Res.string.network_error),
    Canceled(message = Res.string.canceled),
    Unknown(message = Res.string.unknown)
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