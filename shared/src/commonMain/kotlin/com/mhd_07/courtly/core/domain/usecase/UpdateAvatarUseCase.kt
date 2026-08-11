package com.mhd_07.courtly.core.domain.usecase

import com.mhd_07.courtly.core.domain.repo.CoreRepository

class UpdateAvatarUseCase(private val repository: CoreRepository) {
    suspend operator fun invoke(avatar: ByteArray, currentV: Int) {
        repository.updateAvatar(avatar, currentV)
    }
}