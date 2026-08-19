package com.mhd_07.courtly.feature_profile.domain.usecase

import com.mhd_07.courtly.feature_profile.domain.repository.ProfileRepository

class LoadMatchesUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(userId: String) = repository.loadMatches(userId)
}