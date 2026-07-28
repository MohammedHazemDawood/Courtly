package com.mhd_07.courtly.feature_sign.presentation.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhd_07.courtly.feature_sign.domain.usecase.LogOutUseCase
import com.mhd_07.courtly.feature_sign.domain.usecase.LoginUseCase
import com.mhd_07.courtly.feature_sign.domain.usecase.RegisterUseCase
import com.mhd_07.courtly.feature_sign.domain.usecase.SignWithGoogleUseCase
import com.mhd_07.courtly.feature_sign.presentation.module.SignError
import com.mhd_07.courtly.feature_sign.presentation.module.SignIntent
import com.mhd_07.courtly.feature_sign.presentation.module.SignResult
import com.mhd_07.courtly.feature_sign.presentation.module.SignState
import com.mhd_07.courtly.feature_sign.presentation.module.getError
import io.github.jan.supabase.compose.auth.composable.NativeSignInResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SignViewmodel(
    private val register: RegisterUseCase,
    private val login: LoginUseCase,
    private val logout: LogOutUseCase,
    private val signWithGoogle: SignWithGoogleUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SignState())
    val state = _state.asStateFlow()

    val google
        @Composable get() = signWithGoogle { res ->
            when (res) {
                is NativeSignInResult.Success -> _state.update { it.copy(result = SignResult.Success) }
                is NativeSignInResult.Error -> _state.update {
                    it.copy(
                        result = SignResult.Error(
                            SignError.Unknown
                        )
                    )
                }

                is NativeSignInResult.NetworkError -> _state.update {
                    it.copy(
                        result = SignResult.Error(
                            SignError.NetworkError
                        )
                    )
                }

                NativeSignInResult.ClosedByUser -> _state.update {
                    it.copy(
                        result = SignResult.Error(
                            SignError.Canceled
                        )
                    )
                }
            }
        }

    fun handleIntent(intent: SignIntent) {
        when (intent) {
            is SignIntent.Sign -> viewModelScope.launch {
                runCatching {
                    _state.update { it.copy(result = SignResult.Loading) }
                    login(
                        _state.value.email,
                        _state.value.password
                    )
                }
                    .onSuccess { _state.update { it.copy(result = SignResult.Success) } }
                    .onFailure {
                        println(it.message)
                        val error = getError(it.message ?: "")
                        if (it is Exception && error == SignError.AccountExist)
                            runCatching {
                                _state.update { it.copy(result = SignResult.Loading) }
                                register(
                                    _state.value.email,
                                    _state.value.password
                                )
                            }
                                .onSuccess { _state.update { it.copy(result = SignResult.Success) } }
                                .onFailure { faluier ->
                                    _state.update {
                                        it.copy(
                                            result = SignResult.Error(
                                                getError(faluier.message ?: "")
                                            )
                                        )
                                    }
                                }
                        else
                            _state.update { it.copy(result = SignResult.Error(error)) }
                    }
            }

            SignIntent.Logout -> viewModelScope.launch {
                runCatching { logout() }.onSuccess { _state.update { it.copy(result = SignResult.Success) } }
                    .onFailure { fal ->
                        _state.update {
                            it.copy(
                                result = SignResult.Error(
                                    getError(
                                        fal.message ?: ""
                                    )
                                )
                            )
                        }
                    }
            }

        is SignIntent.EditEmail -> _state.update { it.copy(email = intent.email.trim()) }
        is SignIntent.EditPassword -> _state.update { it.copy(password = intent.password.trim()) }
    }
}
}