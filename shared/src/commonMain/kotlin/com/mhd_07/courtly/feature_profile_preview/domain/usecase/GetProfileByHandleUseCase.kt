package com.mhd_07.courtly.feature_profile_preview.domain.usecase

import com.mhd_07.courtly.feature_profile_preview.domain.repository.ProfilePreviewRepository

class GetProfileByHandleUseCase(private val repository: ProfilePreviewRepository) {
    suspend operator fun invoke(handle: String) = repository.getProfileByHandle(handle)
}