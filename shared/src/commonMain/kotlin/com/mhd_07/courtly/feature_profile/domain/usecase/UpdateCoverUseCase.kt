package com.mhd_07.courtly.feature_profile.domain.usecase

import com.mhd_07.courtly.core.domain.repo.CoreRepository
import com.mhd_07.courtly.feature_profile.domain.repository.ProfileRepository

class UpdateCoverUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(cover: ByteArray, currentV: Int) {
        repository.updateCover(cover, currentV)
    }
}