package com.mhd_07.courtly.feature_profile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhd_07.courtly.core.presentation.model.RemoteError
import com.mhd_07.courtly.core.presentation.model.RemoteError.*
import com.mhd_07.courtly.core.presentation.model.RemoteResult
import com.mhd_07.courtly.core.presentation.model.RemoteResult.*
import com.mhd_07.courtly.core.presentation.model.getPostgrestError
import com.mhd_07.courtly.feature_profile.domain.usecase.*
import com.mhd_07.courtly.feature_profile.presentation.viewmodel.model.ProfilePreviewIntent
import com.mhd_07.courtly.feature_profile.presentation.viewmodel.model.ProfilePreviewState
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class ProfilePreviewViewModel(
    private val loadProfileById: GetProfileByIdUseCase,
    private val loadProfileByHandle: GetProfileByHandleUseCase,
    private val loadFollowers: LoadFollowersUseCase,
    private val loadFollowings: LoadFollowingUseCase,
    private val follow: FollowUseCase,
    private val unfollow: UnfollowUseCase,
    private val loadMatches: LoadMatchesUseCase,
    private val logout: LogoutUseCase,
    getUserId: GetUserId
) : ViewModel() {

    private val _state = MutableStateFlow(ProfilePreviewState())
    val state = _state.asStateFlow()

    private val _userId = MutableStateFlow(getUserId())

    private var actionJob: Job? = null
    private var checkHandleJob: Job? = null


    private suspend fun loadUserFollowersList() {
        try {
            _state.update { it.copy(result = RemoteResult.Loading) }
            val followers = _userId.value?.let { loadFollowers(it) } ?: emptyList()
            _state.update { it.copy(userFollowers = followers, result = RemoteResult.Success) }
        } catch (e: Exception) {
            _state.update { it.copy(result = RemoteResult.Error(Unknown)) }
            println("Load Followers Error: ${e.message}")
        }
    }

    private suspend fun loadFollowersList() {
        try {
            _state.update { it.copy(result = RemoteResult.Loading) }
            val followers = _state.value.profile?.id?.let { loadFollowers(it) } ?: emptyList()
            _state.update { it.copy(followers = followers, result = RemoteResult.Success) }
        } catch (e: Exception) {
            _state.update { it.copy(result = RemoteResult.Error(RemoteError.Unknown)) }
            println("Load Followers Error: ${e.message}")
        }
    }

    private suspend fun loadUserFollowingsList() {
        try {
            _state.update { it.copy(result = RemoteResult.Loading) }
            val followings = _userId.value?.let { loadFollowings(it) } ?: emptyList()
            _state.update { it.copy(userFollowing = followings, result = RemoteResult.Success) }
        } catch (e: Exception) {
            _state.update { it.copy(result = RemoteResult.Error(RemoteError.Unknown)) }
            println("Load Followings Error: ${e.message}")
        }
    }

    private suspend fun loadFollowingsList() {
        try {
            _state.update { it.copy(result = RemoteResult.Loading) }
            val followings = _state.value.profile?.id?.let { loadFollowings(it) } ?: emptyList()
            _state.update { it.copy(following = followings, result = RemoteResult.Success) }
        } catch (e: Exception) {
            _state.update { it.copy(result = RemoteResult.Error(RemoteError.Unknown)) }
            println("Load Followings Error: ${e.message}")
        }
    }

    private suspend fun loadPreviewProfileById(id: String) {
        try {
            _state.update { it.copy(result = RemoteResult.Loading) }
            val profile = loadProfileById(id)
            _state.update {
                it.copy(
                    profile = profile,
                    result = Success
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

    private suspend fun loadPreviewProfileByHandle(handle: String) {
        try {
            _state.update { it.copy(result = RemoteResult.Loading) }
            val profile = loadProfileByHandle(handle)
            _state.update {
                it.copy(
                    profile = profile,
                    result = RemoteResult.Success
                )
            }
        } catch (e: PostgrestRestException) {
            println(e.message)
            val error = getPostgrestError(e.code)
            println("loadPreviewProfileByHandle error: ${e.message}")
            _state.update { it.copy(result = RemoteResult.Error(error)) }
        } catch (e: Exception) {
            println("loadPreviewProfileByHandle error: ${e.message}")
            _state.update { it.copy(result = RemoteResult.Error(RemoteError.Unknown)) }
        }
    }

    private fun loadMatches() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(result = RemoteResult.Loading) }
                val matches = _state.value.profile?.id?.let { loadMatches(it) } ?: emptyList()
                _state.update { it.copy(matches = matches, result = RemoteResult.Success) }
            } catch (e: PostgrestRestException) {
                println("Loading Matches Error: ${e.message}")
                _state.update { it.copy(result = RemoteResult.Error(getPostgrestError(e.code))) }
            } catch (e: Exception) {
                println("Loading Matches Error: ${e.message}")
                _state.update { it.copy(result = RemoteResult.Error(RemoteError.Unknown)) }
            }
        }
    }

    fun handleIntent(intent: ProfilePreviewIntent) {
        // IGNORE any incoming intent if an action or cooldown is active
        if (actionJob?.isActive == true) return

        actionJob = viewModelScope.launch {
            try {
                when (intent) {
                    is ProfilePreviewIntent.LoadProfileById -> {
                        if (_state.value.profile != null) return@launch
                        _state.update { it.copy(myId = _userId.value) }
                        loadUserFollowersList()
                        loadUserFollowingsList()
                        loadPreviewProfileById(intent.id)
                        loadFollowersList()
                        loadFollowingsList()
                        loadMatches()
                        _state.update { it.copy(mine = _userId.value == it.profile?.id) }
                    }

                    is ProfilePreviewIntent.LoadProfileByHandle -> {
                        if (_state.value.profile != null) return@launch
                        _state.update { it.copy(myId = _userId.value) }
                        loadUserFollowersList()
                        loadUserFollowingsList()
                        loadPreviewProfileByHandle(intent.handle)
                        loadFollowersList()
                        loadFollowingsList()
                        loadMatches()
                        _state.update { it.copy(mine = _userId.value == it.profile?.id) }
                    }

                    ProfilePreviewIntent.Refresh -> {
                        _state.value.profile?.let {
                            loadUserFollowersList()
                            loadUserFollowingsList()
                            loadPreviewProfileById(it.id)
                            loadFollowersList()
                            loadFollowingsList()
                            loadMatches()
                        }
                    }

                    is ProfilePreviewIntent.Follow -> {
                        try {
                            _state.update { it.copy(result = Loading) }
                            follow(intent.player.id)
                            _state.update { it.copy(result = Success) }
                        } catch (e: PostgrestRestException) {
                            println(e.message)
                            val error = getPostgrestError(e.code)
                            _state.update { it.copy(result = Error(error)) }
                        } catch (e: Exception) {
                            _state.update { it.copy(result = Error(RemoteError.Unknown)) }
                        }
                    }

                    is ProfilePreviewIntent.Unfollow -> {
                        try {
                            _state.update { it.copy(result = Loading) }
                            unfollow(intent.player.id)
                            _state.update { it.copy(result = Success) }
                        } catch (e: PostgrestRestException) {
                            println(e.message)
                            val error = getPostgrestError(e.code)
                            _state.update { it.copy(result = Error(error)) }
                        } catch (e: Exception) {
                            _state.update { it.copy(result = Error(RemoteError.Unknown)) }
                        }
                    }

                    ProfilePreviewIntent.LogOut -> try {
                        logout()
                    } catch (e: AuthRestException) {
                        println("error logout : ${ e.message }")
                    }
                }
            } finally {
                delay(2000.milliseconds)
            }

        }
    }
}