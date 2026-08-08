package com.mhd_07.courtly.core.domain.usecase

import com.mhd_07.courtly.core.domain.repo.CoreRepository

class GetProfileUseCase(private val repository: CoreRepository) {
    suspend operator fun invoke() = repository.getProfile()
}