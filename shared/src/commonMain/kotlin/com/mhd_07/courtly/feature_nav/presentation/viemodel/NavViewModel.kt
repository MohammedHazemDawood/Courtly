package com.mhd_07.courtly.feature_nav.presentation.viemodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhd_07.courtly.feature_nav.domain.usecase.AuthStatus
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class NavViewModel(
    authStatus: AuthStatus
) : ViewModel() {
    val status = authStatus().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SessionStatus.Initializing)
}