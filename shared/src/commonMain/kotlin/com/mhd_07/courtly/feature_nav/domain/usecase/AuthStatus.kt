package com.mhd_07.courtly.feature_nav.domain.usecase

import com.mhd_07.courtly.feature_nav.domain.repo.NavRepository

data class AuthStatus(val repo : NavRepository){
    operator fun invoke() = repo.sessionStatus
}
