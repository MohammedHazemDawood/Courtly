package com.mhd_07.courtly.core.domain.usecase

import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.domain.repo.CoreRepository

class UpdateProfileUseCase(private val repository: CoreRepository) {
    suspend operator fun invoke(profile: Player) {
        return repository.updateProfile(profile)
    }
}