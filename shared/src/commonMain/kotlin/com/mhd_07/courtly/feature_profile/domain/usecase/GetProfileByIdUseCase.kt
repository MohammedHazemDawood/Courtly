package com.mhd_07.courtly.feature_profile.domain.usecase

import com.mhd_07.courtly.feature_profile.domain.repository.ProfileRepository

class GetProfileByIdUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(id: String) = repository.getProfileById(id)
}