package com.mhd_07.courtly.feature_profile_preview.domain.usecase

import com.mhd_07.courtly.feature_profile_preview.domain.repository.ProfilePreviewRepository

class GetProfileByIdUseCase(private val repository: ProfilePreviewRepository) {
    suspend operator fun invoke(id: String) = repository.getProfileById(id)
}