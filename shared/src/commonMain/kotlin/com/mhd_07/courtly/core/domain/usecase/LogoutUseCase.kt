package com.mhd_07.courtly.core.domain.usecase

import com.mhd_07.courtly.core.domain.repo.CoreRepository

class LogoutUseCase(val coreRepository : CoreRepository) {
    suspend operator fun invoke() {
        coreRepository.logout()
    }
}