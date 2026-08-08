package com.mhd_07.courtly.core.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhd_07.courtly.core.domain.model.Profile
import com.mhd_07.courtly.core.domain.usecase.GetProfileUseCase
import com.mhd_07.courtly.core.domain.usecase.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CoreViewmodel(
    private val logout: LogoutUseCase,
    private val getProfile: GetProfileUseCase
) : ViewModel() {
    private val _profile = MutableStateFlow<Profile?>(null)
    val profile = _profile

    init {
        viewModelScope.launch {
            _profile.value = getProfile()
            println("Profile: ${_profile.value}")
        }
    }

    fun logOut() {
        //TODO: Setup Intents and state
        viewModelScope.launch { runCatching { logout() } }
    }
}