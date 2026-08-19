package com.mhd_07.courtly.feature_profile.domain.usecase

import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.feature_profile.domain.repository.ProfileRepository

class UpdateProfileUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(profile: Player) {
        return repository.updateProfile(profile)
    }
}