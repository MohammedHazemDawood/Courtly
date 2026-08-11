package com.mhd_07.courtly.feature_sign.presentation.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhd_07.courtly.feature_sign.domain.usecase.CheckEmailUseCase
import com.mhd_07.courtly.feature_sign.domain.usecase.LoginUseCase
import com.mhd_07.courtly.feature_sign.domain.usecase.RegisterUseCase
import com.mhd_07.courtly.feature_sign.domain.usecase.ResendEmailUseCase
import com.mhd_07.courtly.feature_sign.domain.usecase.SignWithGoogleUseCase
import com.mhd_07.courtly.feature_sign.domain.usecase.VerifyEmailUseCase
import com.mhd_07.courtly.core.presentation.model.RemoteError
import com.mhd_07.courtly.feature_sign.presentation.model.SignIntent
import com.mhd_07.courtly.core.presentation.model.RemoteResult.*
import com.mhd_07.courtly.feature_sign.presentation.model.SignState
import com.mhd_07.courtly.core.presentation.model.getAuthError
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.compose.auth.composable.NativeSignInResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignViewmodel(
    private val register: RegisterUseCase,
    private val login: LoginUseCase,
    private val signWithGoogle: SignWithGoogleUseCase,
    private val checkEmail: CheckEmailUseCase,
    private val resendEmail: ResendEmailUseCase,
    private val verifyEmail: VerifyEmailUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SignState())
    val state = _state.asStateFlow()

    val google
        @Composable get() = signWithGoogle { res ->
            when (res) {
                is NativeSignInResult.Success -> _state.update { it.copy(result = Success) }
                is NativeSignInResult.Error -> _state.update {
                    it.copy(result = Error(RemoteError.Unknown))
                }

                is NativeSignInResult.NetworkError -> _state.update {
                    it.copy(result = Error(RemoteError.NetworkError))
                }

                NativeSignInResult.ClosedByUser -> _state.update {
                    it.copy(result = Error(RemoteError.Canceled))
                }
            }
        }

    fun handleIntent(intent: SignIntent) {
        when (intent) {
            is SignIntent.Sign -> viewModelScope.launch {
                try {
                    _state.update { it.copy(result = Loading) }

                    val exists = checkEmail(_state.value.email)
                    println("exists: $exists")
                    if (exists)
                        login(_state.value.email, _state.value.password)
                    else {
                        register(_state.value.email, _state.value.password)
                        login(_state.value.email, _state.value.password)
                    }
                    _state.update { it.copy(result = Success) }
                } catch (e: AuthRestException) {
                    println("error: ${e.message}")
                    _state.update { it.copy(result = Error(getAuthError(e.errorCode))) }
                } catch (e: Exception) {
                    println("error: ${e.message}")
                    _state.update { it.copy(result = Error(RemoteError.Unknown)) }
                }
            }

            is SignIntent.EditEmail -> _state.update { it.copy(email = intent.email.trim()) }
            is SignIntent.EditPassword -> _state.update { it.copy(password = intent.password.trim()) }
            is SignIntent.EditOTP -> _state.update { it.copy(otp = intent.otp.trim()) }
            SignIntent.ResendOTP -> viewModelScope.launch {//TODO: Implement time limit
                try {
                    _state.update { it.copy(result = Loading) }
                    resendEmail(_state.value.email)
                    _state.update { it.copy(result = Success) }
                } catch (e: AuthRestException) {
                    println("error: ${e.message}")
                    _state.update { it.copy(result = Error(getAuthError(e.errorCode))) }
                } catch (e: Exception) {
                    println("error: ${e.message}")
                    _state.update { it.copy(result = Error(RemoteError.Unknown)) }
                }
            }

            SignIntent.VerifyOTP -> viewModelScope.launch {
                try {
                    _state.update { it.copy(result = Loading) }
                    verifyEmail(_state.value.email, _state.value.otp)
                    _state.update { it.copy(result = Success) }
                } catch (e: AuthRestException) {
                    println("error: ${e.message}")
                    _state.update { it.copy(result = Error(getAuthError(e.errorCode))) }
                } catch (e: Exception) {
                    println("error: ${e.message}")
                    _state.update { it.copy(result = Error(RemoteError.Unknown)) }
                }
            }
        }
    }
}