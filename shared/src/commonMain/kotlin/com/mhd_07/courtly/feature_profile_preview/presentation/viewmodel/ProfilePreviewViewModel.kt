package com.mhd_07.courtly.feature_profile_preview.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhd_07.courtly.core.presentation.model.RemoteError
import com.mhd_07.courtly.core.presentation.model.RemoteResult
import com.mhd_07.courtly.core.presentation.model.getPostgrestError
import com.mhd_07.courtly.feature_profile_preview.domain.usecase.*
import com.mhd_07.courtly.feature_profile_preview.presentation.viewmodel.model.ProfilePreviewIntent
import com.mhd_07.courtly.feature_profile_preview.presentation.viewmodel.model.ProfilePreviewState
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class ProfilePreviewViewModel(
    private val loadProfile: GetProfileUseCase,
    private val loadFollowers: LoadFollowersUseCase,
    private val loadFollowings: LoadFollowingUseCase,
    private val follow: FollowUseCase,
    private val unfollow: UnfollowUseCase,
    getUserId: GetUserId
) : ViewModel() {

    private val _state = MutableStateFlow(ProfilePreviewState())
    val state = _state.asStateFlow()

    private val _userId = MutableStateFlow(getUserId())

    private var actionJob: Job? = null

    private suspend fun loadUserFollowersList() {
        try {
            _state.update { it.copy(result = RemoteResult.Loading) }
            val followers = _userId.value?.let { loadFollowers(it) } ?: emptyList()
            _state.update { it.copy(userFollowers = followers, result = RemoteResult.Success) }
        } catch (e: Exception) {
            _state.update { it.copy(result = RemoteResult.Error(RemoteError.Unknown)) }
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

    private suspend fun loadPreviewProfile(id: String) {
        try {
            _state.update { it.copy(result = RemoteResult.Loading) }
            val profile = loadProfile(id)
            _state.update { it.copy(profile = profile, result = RemoteResult.Success) }
        } catch (e: PostgrestRestException) {
            println(e.message)
            val error = getPostgrestError(e.code)
            _state.update { it.copy(result = RemoteResult.Error(error)) }
        } catch (e: Exception) {
            _state.update { it.copy(result = RemoteResult.Error(RemoteError.Unknown)) }
        }
    }

    fun handleIntent(intent: ProfilePreviewIntent) {
        // IGNORE any incoming intent if an action or cooldown is active
        if (actionJob?.isActive == true) return

        actionJob = viewModelScope.launch {
            try {
                when (intent) {
                    is ProfilePreviewIntent.LoadProfile -> {
                        if (_state.value.profile != null) return@launch
                        _state.update { it.copy(myId = _userId.value) }
                        loadUserFollowersList()
                        loadUserFollowingsList()
                        loadPreviewProfile(intent.id)
                        loadFollowersList()
                        loadFollowingsList()
                        _state.update { it.copy(mine = _userId.value == it.profile?.id) }
                    }

                    ProfilePreviewIntent.Refresh -> {
                        _state.value.profile?.let {
                            loadUserFollowersList()
                            loadUserFollowingsList()
                            loadPreviewProfile(it.id)
                            loadFollowersList()
                            loadFollowingsList()
                        }
                    }

                    is ProfilePreviewIntent.Follow -> {
                        try {
                            _state.update { it.copy(result = RemoteResult.Loading) }
                            follow(intent.player.id)
                            _state.update { it.copy(result = RemoteResult.Success) }
                        } catch (e: PostgrestRestException) {
                            println(e.message)
                            val error = getPostgrestError(e.code)
                            _state.update { it.copy(result = RemoteResult.Error(error)) }
                        } catch (e: Exception) {
                            _state.update { it.copy(result = RemoteResult.Error(RemoteError.Unknown)) }
                        }
                    }

                    is ProfilePreviewIntent.Unfollow -> {
                        try {
                            _state.update { it.copy(result = RemoteResult.Loading) }
                            unfollow(intent.player.id)
                            _state.update { it.copy(result = RemoteResult.Success) }
                        } catch (e: PostgrestRestException) {
                            println(e.message)
                            val error = getPostgrestError(e.code)
                            _state.update { it.copy(result = RemoteResult.Error(error)) }
                        } catch (e: Exception) {
                            _state.update { it.copy(result = RemoteResult.Error(RemoteError.Unknown)) }
                        }
                    }

                    is ProfilePreviewIntent.LoadMyProfile -> {
                        if (_state.value.profile != null) return@launch
                        _state.update { it.copy(myId = _userId.value) }
                        loadUserFollowersList()
                        loadUserFollowingsList()
                        _userId.value?.let {
                            loadPreviewProfile(it)
                            loadFollowersList()
                            loadFollowingsList()
                        }
                        _state.update { it.copy(mine = _userId.value == it.profile?.id) }
                    }
                }
            } finally {
                delay(2000.milliseconds)
            }
        }
    }
}