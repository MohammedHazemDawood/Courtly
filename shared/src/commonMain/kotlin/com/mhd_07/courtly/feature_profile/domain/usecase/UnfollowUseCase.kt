package com.mhd_07.courtly.feature_profile.domain.usecase

import com.mhd_07.courtly.feature_profile.domain.repository.ProfileRepository

class UnfollowUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(id: String) = repository.unfollow(id)
}