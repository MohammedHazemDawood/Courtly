package com.mhd_07.courtly.feature_profile.domain.usecase

import com.mhd_07.courtly.core.domain.repo.CoreRepository
import com.mhd_07.courtly.feature_profile.domain.repository.ProfileRepository

class LogoutUseCase(val coreRepository : ProfileRepository) {
    suspend operator fun invoke() {
        coreRepository.logout()
    }
}