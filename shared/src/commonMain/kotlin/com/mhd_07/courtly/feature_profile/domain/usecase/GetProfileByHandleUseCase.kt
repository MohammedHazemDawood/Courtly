package com.mhd_07.courtly.feature_profile.domain.usecase

import com.mhd_07.courtly.feature_profile.domain.repository.ProfileRepository

class GetProfileByHandleUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(handle: String) = repository.getProfileByHandle(handle)
}