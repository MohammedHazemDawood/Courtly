package com.mhd_07.courtly.feature_profile.domain.usecase

import com.mhd_07.courtly.core.domain.repo.CoreRepository
import com.mhd_07.courtly.feature_profile.domain.repository.ProfileRepository

class UpdateAvatarUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(avatar: ByteArray, currentV: Int) {
        repository.updateAvatar(avatar, currentV)
    }
}