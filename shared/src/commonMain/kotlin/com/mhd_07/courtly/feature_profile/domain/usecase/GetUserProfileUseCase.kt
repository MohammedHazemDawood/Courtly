package com.mhd_07.courtly.feature_profile.domain.usecase

import com.mhd_07.courtly.feature_profile.domain.repository.ProfileRepository

class GetUserProfileUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke() = repository.getMyProfile()
}