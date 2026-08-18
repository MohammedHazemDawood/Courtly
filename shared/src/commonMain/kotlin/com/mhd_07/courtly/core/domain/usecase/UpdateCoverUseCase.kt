package com.mhd_07.courtly.core.domain.usecase

import com.mhd_07.courtly.core.domain.repo.CoreRepository

class UpdateCoverUseCase(private val repository: CoreRepository) {
    suspend operator fun invoke(cover: ByteArray, currentV: Int) {
        repository.updateCover(cover, currentV)
    }
}