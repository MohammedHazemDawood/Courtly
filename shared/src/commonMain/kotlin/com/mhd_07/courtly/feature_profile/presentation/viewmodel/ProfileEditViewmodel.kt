package com.mhd_07.courtly.feature_profile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhd_07.courtly.core.presentation.model.RemoteError
import com.mhd_07.courtly.core.presentation.model.RemoteResult
import com.mhd_07.courtly.core.presentation.model.getPostgrestError
import com.mhd_07.courtly.core.presentation.viewmodel.HANDLE_REGEX
import com.mhd_07.courtly.feature_profile.domain.usecase.CheckHandleUseCase
import com.mhd_07.courtly.feature_profile.domain.usecase.GetUserProfileUseCase
import com.mhd_07.courtly.feature_profile.domain.usecase.UpdateAvatarUseCase
import com.mhd_07.courtly.feature_profile.domain.usecase.UpdateCoverUseCase
import com.mhd_07.courtly.feature_profile.domain.usecase.UpdateProfileUseCase
import com.mhd_07.courtly.feature_profile.presentation.viewmodel.model.ProfileEditIntent
import com.mhd_07.courtly.feature_profile.presentation.viewmodel.model.ProfileEditState
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class ProfileEditViewmodel(
    private val updateProfile: UpdateProfileUseCase,
    private val updateAvatar: UpdateAvatarUseCase,
    private val updateCover: UpdateCoverUseCase,
    private val checkHandle: CheckHandleUseCase,
    private val loadUserProfile: GetUserProfileUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileEditState())
    val state = _state.asStateFlow()

    var checkHandleJob: Job? = null

    init{
        viewModelScope.launch {
            loadProfile()
        }
    }

    private suspend fun loadProfile() {
        try {
            _state.update { it.copy(result = RemoteResult.Loading) }
            val profile = loadUserProfile()
            _state.update {
                it.copy(
                    profile = profile,
                    avatarPath = profile?.avatar,
                    avatarVersion = profile?.avatarVersion ?: 0,
                    displayName = profile?.name ?: "",
                    handle = profile?.handle ?: "",
                    bio = profile?.bio ?: "",
                    handleAvailable = true,
                    saveEnabled = false,
                    cover = profile?.cover,
                    coverVersion = profile?.coverVersion ?: 0,
                    result = RemoteResult.Success
                )
            }
        } catch (e: PostgrestRestException) {
            val error = getPostgrestError(e.code)
            println("loadPreviewProfileByHandle error: ${e.message}")
            _state.update { it.copy(result = RemoteResult.Error(error)) }
        } catch (e: Exception) {
            println("loadPreviewProfileByHandle error: ${e.message}")
            _state.update { it.copy(result = RemoteResult.Error(RemoteError.Unknown)) }
        }
    }

    private fun checkSavedEnabled(state: ProfileEditState): Boolean {
        val profile = state.profile ?: return false
        val handleChanged = state.handle != profile.handle

        if (handleChanged && !state.handleAvailable) return false

        return handleChanged ||
                state.displayName.trim() != profile.name ||
                state.bio.trim() != profile.bio ||
                state.avatarPath != profile.avatar
    }

    fun handleIntent(intent: ProfileEditIntent) {
        // IGNORE any incoming intent if an action or cooldown is active
        when (intent) {
            is ProfileEditIntent.ChangeBio -> _state.update { current ->
                val updated = current.copy(bio = intent.bio)
                updated.copy(saveEnabled = checkSavedEnabled(updated))
            }

            is ProfileEditIntent.ChangeHandle -> {
                checkHandleJob?.cancel()
                val trimmedHandle = intent.handle.trim()
                val isRegexMatch =
                    trimmedHandle.isNotEmpty() && Regex(HANDLE_REGEX).matches(trimmedHandle)
                val isSameAsCurrent = trimmedHandle == _state.value.profile?.handle
                val initialAvailable = isRegexMatch || isSameAsCurrent

                _state.update { current ->
                    val updated = current.copy(
                        handle = trimmedHandle,
                        handleAvailable = initialAvailable
                    )
                    updated.copy(saveEnabled = checkSavedEnabled(updated))
                }

                checkHandleJob = viewModelScope.launch {
                    delay(1000L.milliseconds)
                    if (trimmedHandle.isNotEmpty() && trimmedHandle != _state.value.profile?.handle) {
                        try {
                            val available = checkHandle(trimmedHandle)
                            _state.update { current ->
                                val updated = current.copy(handleAvailable = available)
                                updated.copy(saveEnabled = checkSavedEnabled(updated))
                            }
                        } catch (e: Exception) {
                            println("Check handle error: ${e.message}")
                        }
                    }
                }
            }

            is ProfileEditIntent.ChangeName -> _state.update { current ->
                val updated = current.copy(displayName = intent.name)
                updated.copy(saveEnabled = checkSavedEnabled(updated))
            }

            is ProfileEditIntent.ChangeAvatar -> viewModelScope.launch {
                try {
                    _state.update { it.copy(result = RemoteResult.Loading) }
                    updateAvatar(intent.avatar, _state.value.avatarVersion)
                    _state.value.profile?.id?.let { loadProfile() }
                } catch (e: Exception) {
                    _state.update { it.copy(result = RemoteResult.Error(RemoteError.Unknown)) }
                    println("Avatar Error: ${e.message}")
                }
            }

            is ProfileEditIntent.ChangeCover -> viewModelScope.launch {
                try {
                    _state.update { it.copy(result = RemoteResult.Loading) }
                    updateCover(intent.cover, _state.value.coverVersion)
                    _state.value.profile?.id?.let { loadProfile() }
                } catch (e: Exception) {
                    _state.update { it.copy(result = RemoteResult.Error(RemoteError.Unknown)) }
                    println("Cover Error: ${e.message}")
                }
            }

            ProfileEditIntent.UpdateProfile -> viewModelScope.launch {
                val profile = _state.value.profile ?: return@launch
                if (!checkSavedEnabled(_state.value)) return@launch

                try {
                    _state.update { it.copy(result = RemoteResult.Loading) }
                    updateProfile(
                        profile.copy(
                            handle = _state.value.handle,
                            name = _state.value.displayName.trim(),
                            avatar = _state.value.avatarPath ?: "",
                            bio = _state.value.bio.trim(),
                            cover = _state.value.cover ?: "",
                        ),
                    )
                    _state.value.profile?.id?.let {
                        loadProfile()
                    }
                    _state.update {
                        it.copy(
                            result = RemoteResult.Success,
                            saveEnabled = checkSavedEnabled(_state.value)
                        )
                    }
                } catch (e: PostgrestRestException) {
                    val error = getPostgrestError(e.code)
                    _state.update { current ->
                        val isUniqueError = error == RemoteError.UniquenessViolation
                        val updated = current.copy(
                            handleAvailable = if (isUniqueError) false else current.handleAvailable,
                            result = RemoteResult.Error(error)
                        )
                        updated.copy(saveEnabled = checkSavedEnabled(updated))
                    }
                } catch (_: Exception) {
                    _state.update { it.copy(result = RemoteResult.Error(RemoteError.Unknown)) }
                }
            }

        }
    }
}