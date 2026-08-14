package com.mhd_07.courtly.feature_profile_preview.domain.usecase

import com.mhd_07.courtly.core.domain.repo.CoreRepository
import com.mhd_07.courtly.feature_profile_preview.domain.repository.ProfilePreviewRepository

class GetProfileUseCase(private val repository: ProfilePreviewRepository) {
    suspend operator fun invoke(id: String) = repository.getProfile(id)
}