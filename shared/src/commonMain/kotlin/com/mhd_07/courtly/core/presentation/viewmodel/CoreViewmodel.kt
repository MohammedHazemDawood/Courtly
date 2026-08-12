package com.mhd_07.courtly.core.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.domain.usecase.CheckHandleUseCase
import com.mhd_07.courtly.core.domain.usecase.GetProfileUseCase
import com.mhd_07.courtly.core.domain.usecase.LoadFollowersUseCase
import com.mhd_07.courtly.core.domain.usecase.LoadFollowingUseCase
import com.mhd_07.courtly.core.domain.usecase.LogoutUseCase
import com.mhd_07.courtly.core.domain.usecase.UpdateAvatarUseCase
import com.mhd_07.courtly.core.domain.usecase.UpdateProfileUseCase
import com.mhd_07.courtly.core.presentation.model.CoreIntent
import com.mhd_07.courtly.core.presentation.model.CoreState
import com.mhd_07.courtly.core.presentation.model.RemoteError
import com.mhd_07.courtly.core.presentation.model.RemoteResult
import com.mhd_07.courtly.core.presentation.model.RemoteResult.*
import com.mhd_07.courtly.core.presentation.model.getPostgrestError
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class CoreViewmodel(
    private val logout: LogoutUseCase,
    private val getProfile: GetProfileUseCase,
    private val checkHandle: CheckHandleUseCase,
    private val updateProfile: UpdateProfileUseCase,
    private val updateAvatar: UpdateAvatarUseCase,
    private val loadFollowers: LoadFollowersUseCase,
    private val loadFollowings: LoadFollowingUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(CoreState())

    val state = _state.asStateFlow()

    private var checkHandleJob: Job? = null
    private var reloadJob : Job? = null

    init {
        loadProfile()
        loadFollowersList()
        loadFollowingsList()
    }

    private fun loadFollowersList() {
        _state.update { it.copy(result = RemoteResult.Loading) }
        try {
            viewModelScope.launch {
                val followers = loadFollowers()
                _state.update { it.copy(followers = followers, result = RemoteResult.Success) }
            }
        } catch (e: Exception) {
            _state.update { it.copy(result = RemoteResult.Error(RemoteError.Unknown)) }
            println("Load Followers Error: ${e.message}")
        }
    }

    private fun loadFollowingsList() {
        _state.update { it.copy(result = RemoteResult.Loading) }
        try {
            viewModelScope.launch {
                val followings = loadFollowings()
                _state.update { it.copy(following = followings, result = RemoteResult.Success) }
            }
        }catch (e: Exception) {
            _state.update { it.copy(result = RemoteResult.Error(RemoteError.Unknown)) }
            println("Load Followings Error: ${e.message}")
        }
    }


    //TODO: Handle Error

    private fun loadProfile() {
        viewModelScope.launch {
            var profile: Player? = null
            try {
                _state.update { it.copy(result = RemoteResult.Loading) }
                profile = getProfile()
                _state.update { it.copy(result = RemoteResult.Success) }
            } catch (e: PostgrestRestException) {//TODO
                println("Error LoadingProfile: ${e.message}")
                _state.update { it.copy(result = RemoteResult.Error(RemoteError.Unknown)) }
            } catch (e: Exception) {
                println("Error LoadingProfile: ${e.message}")
                _state.update { it.copy(result = RemoteResult.Error(RemoteError.Unknown)) }
            }
            _state.update {
                it.copy(
                    profile = profile,
                    avatarPath = profile?.avatar,
                    avatarVersion = profile?.avatarVersion ?: 0,
                    displayName = profile?.name ?: "",
                    handle = profile?.handle ?: "",
                    bio = profile?.bio ?: "",
                    handleAvailable = true,
                    saveEnabled = false
                )
            }
        }
    }

    fun handleIntent(intent: CoreIntent) {
        when (intent) {
            is CoreIntent.ChangeBio -> _state.update {
                it.copy(
                    bio = intent.bio,
                    saveEnabled = checkSavedEnabled()
                )
            }

            is CoreIntent.ChangeHandle -> {
                checkHandleJob?.cancel()
                _state.update {
                    it.copy(
                        handle = intent.handle.trim(),
                        handleAvailable = intent.handle.isNotEmpty() && Regex(HANDLE_REGEX).matches(
                            intent.handle
                        ) || intent.handle == _state.value.profile?.handle,
                        saveEnabled = checkSavedEnabled()
                    )
                }
                checkHandleJob = viewModelScope.launch {
                    delay(1000L.milliseconds)
                    if (intent.handle.isNotEmpty() && intent.handle != _state.value.profile?.handle)
                        try {
                            _state.update { it.copy(handleAvailable = checkHandle(intent.handle)) }
                        } catch (e: Exception) {
                            println(e.message)
                        }
                }
                _state.update { it.copy(saveEnabled = checkSavedEnabled()) }
            }

            is CoreIntent.ChangeName -> _state.update {
                it.copy(
                    displayName = intent.name,
                    saveEnabled = checkSavedEnabled()
                )
            }

            is CoreIntent.ChangeAvatar -> {
                _state.update { it.copy(result = RemoteResult.Loading) }
                try {
                    viewModelScope.launch {
                        updateAvatar(intent.avatar, _state.value.avatarVersion)
                        loadProfile()
                    }
                    _state.update { it.copy(result = RemoteResult.Success) }
                } catch (e: Exception) {
                    _state.update { it.copy(result = Error(RemoteError.Unknown)) }//TODO:Just Temp
                    println("Avatart Error: ${e.message}")
                }
            }

            CoreIntent.LogOut -> viewModelScope.launch {
                logout()
            }

            CoreIntent.UpdateProfile -> viewModelScope.launch {
                if (!checkSavedEnabled())
                    return@launch
                try {
                    _state.update { it.copy(result = RemoteResult.Loading) }
                    updateProfile(
                        Player(
                            id = _state.value.profile!!.id,
                            handle = _state.value.handle,
                            name = _state.value.displayName.trim(),
                            avatar = _state.value.avatarPath ?: "",
                            bio = _state.value.bio.trim(),
                            avatarVersion = _state.value.avatarVersion
                        )
                    )
                    loadProfile()
                    _state.update { it.copy(result = RemoteResult.Success) }
                } catch (e: PostgrestRestException) {
                    println(e.message)
                    val error = getPostgrestError(e.code)
                    if (error == RemoteError.UniquenessViolation)
                        _state.update { it.copy(handleAvailable = false) }
                    _state.update { it.copy(result = Error(error)) }
                } catch (_: Exception) {
                    _state.update { it.copy(result = Error(RemoteError.Unknown)) }
                }
            }

            CoreIntent.Refresh -> {
                reloadJob?.cancel()
                reloadJob = viewModelScope.launch {
                    delay(1000.milliseconds)
                    loadProfile()
                    loadFollowingsList()
                    loadFollowersList()
                }
            }
        }
    }

    private fun checkSavedEnabled() = _state.value.let {
        it.handle != it.profile?.handle ||
                it.displayName.trim() != it.profile.name ||
                it.bio.trim() != it.profile.bio ||
                it.avatarPath != it.profile.avatar ||
                it.handleAvailable
    }
}

const val HANDLE_REGEX = "^[a-zA-Z0-9_]{3,20}$"