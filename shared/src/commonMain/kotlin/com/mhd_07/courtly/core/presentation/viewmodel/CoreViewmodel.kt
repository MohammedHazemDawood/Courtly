package com.mhd_07.courtly.core.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhd_07.courtly.core.domain.usecase.LogoutUseCase
import kotlinx.coroutines.launch

class CoreViewmodel(
    private val logout: LogoutUseCase,
) : ViewModel() {
    fun logOut() {
        //TODO: Setup Intents and state
        viewModelScope.launch { runCatching { logout() } }
    }
}