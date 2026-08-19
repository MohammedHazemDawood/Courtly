package com.mhd_07.courtly.core.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhd_07.courtly.core.domain.usecase.GetProfileUseCase
import com.mhd_07.courtly.core.domain.usecase.LoadFeedUseCase
import com.mhd_07.courtly.core.presentation.model.CoreIntent
import com.mhd_07.courtly.core.presentation.model.CoreState
import com.mhd_07.courtly.core.presentation.model.RemoteError
import com.mhd_07.courtly.core.presentation.model.RemoteResult.*
import com.mhd_07.courtly.core.presentation.model.getPostgrestError
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CoreViewmodel(
    private val getProfile: GetProfileUseCase,
    private val loadFeedUseCase: LoadFeedUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CoreState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            loadProfile()
            loadFeed(isRefresh = true)
        }
    }

    private suspend fun loadProfile() {
        _state.update { it.copy(result = Loading) }
        try {
            val profile = getProfile()
            _state.update { current ->
                current.copy(
                    profile = profile,
                    result = Success
                )
            }
        } catch (e: PostgrestRestException) {
            println("Error LoadingProfile: ${e.message}")
            _state.update { it.copy(result = Error(getPostgrestError(e.code))) }
        } catch (e: Exception) {
            println("Error LoadingProfile: ${e.message}")
            _state.update { it.copy(result = Error(RemoteError.Unknown)) }
        }
    }

    private suspend fun loadFeed(isRefresh: Boolean = false) {
        try {
            _state.update { it.copy(result = Loading) }
            val nextPage = if (isRefresh) 0 else _state.value.page + 1
            val pageData = loadFeedUseCase(nextPage)
            println("got page : $pageData :$nextPage")
            _state.update { current ->
                current.copy(
                    page = nextPage,
                    matches = if (isRefresh) pageData else current.matches + pageData,
                    result = Success
                )
            }
        } catch (e: PostgrestRestException) {
            println("load feed error: ${e.message}")
            _state.update { it.copy(result = Error(getPostgrestError(e.code))) }
        } catch (e: Exception) {
            println("load feed error: ${e.message}")
            _state.update { it.copy(result = Error(RemoteError.Unknown)) }
        }
    }

    fun handleIntent(intent: CoreIntent) {
        when (intent) {
            CoreIntent.LoadFeed -> viewModelScope.launch {
                loadFeed(isRefresh = false)
            }

            CoreIntent.Refresh -> viewModelScope.launch {
                loadFeed(isRefresh = true)
            }
        }
    }
}

const val HANDLE_REGEX = "^[a-zA-Z0-9_]{3,20}$"