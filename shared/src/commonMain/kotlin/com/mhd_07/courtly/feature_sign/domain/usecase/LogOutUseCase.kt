package com.mhd_07.courtly.feature_sign.domain.usecase

import com.mhd_07.courtly.feature_sign.domain.repo.SignRepository

data class LogOutUseCase(val repo: SignRepository){
    suspend operator fun invoke() = repo.logout()
}
