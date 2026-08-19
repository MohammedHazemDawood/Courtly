package com.mhd_07.courtly.feature_profile.domain.usecase

import com.mhd_07.courtly.feature_profile.domain.repository.ProfileRepository

class FollowUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(id: String) = repository.follow(id)
}