package com.mhd_07.courtly.core.presentation.model

import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.account_exist
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
import kotlin.collections.get

sealed interface RemoteResult {
    data object Loading : RemoteResult
    data object Success/*(val acc : Account)*/ : RemoteResult

    //    data object Ideal : SignResult
    data class Error(val error: RemoteError) : RemoteResult
}

enum class RemoteError(val message: StringResource) {
    AccountExist(message = Res.string.account_exist),
    InvalidCredentials(message = Res.string.invalid_credentials),
    NotConfirmed(message = Res.string.not_confirmed),
    TooManyRequest(message = Res.string.too_many_request),
    SamePassword(message = Res.string.same_password),
    UserPanned(message = Res.string.user_banned),
    WeakPassword(message = Res.string.weak_password),
    NetworkError(message = Res.string.network_error),
    Canceled(message = Res.string.canceled),

    UniquenessViolation(message = Res.string.unknown),
    ConnectionError(message = Res.string.unknown),

    Unknown(message = Res.string.unknown),
    NotFound(message = Res.string.unknown)//TODO: To be changed
}

private val postgrestErrorMessages = mapOf(
    "23505" to RemoteError.UniquenessViolation,
    "08*" to RemoteError.ConnectionError,
)

private val authErrorMessages = mapOf(
    //TODO: To be change all string res
    AuthErrorCode.EmailNotConfirmed to RemoteError.NotConfirmed,
    AuthErrorCode.InvalidCredentials to RemoteError.InvalidCredentials,
    AuthErrorCode.EmailExists to RemoteError.AccountExist,
    AuthErrorCode.WeakPassword to RemoteError.WeakPassword,
    AuthErrorCode.UserBanned to RemoteError.UserPanned,
    AuthErrorCode.SamePassword to RemoteError.SamePassword,
    AuthErrorCode.OverRequestRateLimit to RemoteError.TooManyRequest,
)

fun getAuthError(error: AuthErrorCode?) = authErrorMessages[error] ?: RemoteError.Unknown
fun getPostgrestError(error: String?) = postgrestErrorMessages[error] ?: RemoteError.Unknown