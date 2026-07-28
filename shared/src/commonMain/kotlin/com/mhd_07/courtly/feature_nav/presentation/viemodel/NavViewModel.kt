package com.mhd_07.courtly.feature_nav.presentation.viemodel

import androidx.lifecycle.ViewModel
import com.mhd_07.courtly.feature_nav.domain.usecase.AuthStatus

class NavViewModel(
    authStatus: AuthStatus
) : ViewModel() {
    val status = authStatus()
}