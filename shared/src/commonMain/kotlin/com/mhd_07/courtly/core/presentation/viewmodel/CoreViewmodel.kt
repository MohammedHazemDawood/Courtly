package com.mhd_07.courtly.core.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhd_07.courtly.feature_sign.domain.usecase.LogOutUseCase
import kotlinx.coroutines.launch

class CoreViewmodel(
    private val logout: LogOutUseCase,
) : ViewModel() {
    fun logOut() {
        //TODO: Setup Intents and state
        viewModelScope.launch { runCatching { logout() } }
    }
}