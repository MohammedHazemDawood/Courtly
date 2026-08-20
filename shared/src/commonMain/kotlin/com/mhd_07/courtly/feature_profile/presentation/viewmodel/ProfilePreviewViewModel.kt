package com.mhd_07.courtly.feature_profile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhd_07.courtly.core.domain.model.Player
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
import kotlinx.coroutines.coroutineScope
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

    // Helper methods now only update data arrays, NOT the main 'result' state
    private suspend fun loadUserFollowersList() {
        runCatching {
            val followers = _userId.value?.let { loadFollowers(it) } ?: emptyList()
            _state.update { it.copy(userFollowers = followers) }
        }.onFailure { e ->
            println("Load User Followers Error: ${e.message}")
        }
    }

    private suspend fun loadUserFollowingsList() {
        runCatching {
            val followings = _userId.value?.let { loadFollowings(it) } ?: emptyList()
            _state.update { it.copy(userFollowing = followings) }
        }.onFailure { e ->
            println("Load User Followings Error: ${e.message}")
        }
    }

    private suspend fun loadFollowersList(profileId: String) {
        runCatching {
            val followers = loadFollowers(profileId)
            _state.update { it.copy(followers = followers) }
        }.onFailure { e ->
            println("Load Profile Followers Error: ${e.message}")
        }
    }

    private suspend fun loadFollowingsList(profileId: String) {
        runCatching {
            val followings = loadFollowings(profileId)
            _state.update { it.copy(following = followings) }
        }.onFailure { e ->
            println("Load Profile Followings Error: ${e.message}")
        }
    }

    private suspend fun loadMatchesList(profileId: String) {
        runCatching {
            val matches = loadMatches(profileId)
            _state.update { it.copy(matches = matches) }
        }.onFailure { e ->
            println("Load Matches Error: ${e.message}")
        }
    }

    fun handleIntent(intent: ProfilePreviewIntent) {
        if (actionJob?.isActive == true) return

        actionJob = viewModelScope.launch {
            try {
                when (intent) {
                    is ProfilePreviewIntent.LoadProfileById -> fetchProfileData {
                        loadProfileById(intent.id)
                    }

                    is ProfilePreviewIntent.LoadProfileByHandle -> fetchProfileData {
                        loadProfileByHandle(intent.handle)
                    }

                    ProfilePreviewIntent.Refresh -> {
                        _state.value.profile?.id?.let { profileId ->
                            fetchProfileData { _state.value.profile }
                        }
                    }

                    is ProfilePreviewIntent.Follow -> handleFollowAction {
                        follow(intent.player.id)
                    }

                    is ProfilePreviewIntent.Unfollow -> handleFollowAction {
                        unfollow(intent.player.id)
                    }

                    ProfilePreviewIntent.LogOut -> runCatching { logout() }
                        .onFailure { println("Logout error: ${it.message}") }
                }
            } finally {
                delay(2000.milliseconds)
            }
        }
    }

    private suspend fun fetchProfileData(fetchProfile: suspend () -> Player?) {
        if (_state.value.profile != null) return

        _state.update { it.copy(result = RemoteResult.Loading, myId = _userId.value) }

        try {
            val profile = fetchProfile() ?: throw Exception("Profile not found")
            _state.update {
                it.copy(
                    profile = profile,
                    mine = _userId.value == profile.id
                )
            }

            coroutineScope {
                launch { loadUserFollowersList() }
                launch { loadUserFollowingsList() }
                launch { loadFollowersList(profile.id) }
                launch { loadFollowingsList(profile.id) }
                launch { loadMatchesList(profile.id) }
            }

            _state.update { it.copy(result = RemoteResult.Success) }

        } catch (e: PostgrestRestException) {
            println("Fetch profile error: ${e.message}")
            _state.update { it.copy(result = RemoteResult.Error(getPostgrestError(e.code))) }
        } catch (e: Exception) {
            println("Fetch profile error: ${e.message}")
            _state.update { it.copy(result = RemoteResult.Error(RemoteError.Unknown)) }
        }
    }

    private suspend fun handleFollowAction(action: suspend () -> Unit) {
        try {
            _state.update { it.copy(result = RemoteResult.Loading) }
            action()
            _state.update { it.copy(result = RemoteResult.Success) }
        } catch (e: PostgrestRestException) {
            _state.update { it.copy(result = RemoteResult.Error(getPostgrestError(e.code))) }
        } catch (e: Exception) {
            _state.update { it.copy(result = RemoteResult.Error(RemoteError.Unknown)) }
        }
    }
}