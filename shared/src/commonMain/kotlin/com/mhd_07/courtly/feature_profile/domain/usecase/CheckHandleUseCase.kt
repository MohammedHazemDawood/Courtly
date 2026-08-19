package com.mhd_07.courtly.feature_profile.domain.usecase

import com.mhd_07.courtly.core.domain.repo.CoreRepository
import com.mhd_07.courtly.feature_profile.domain.repository.ProfileRepository

class CheckHandleUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(handle: String): Boolean {
        return repository.checkHandle(handle)
    }
}