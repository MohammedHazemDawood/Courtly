package com.mhd_07.courtly.core.domain.usecase

import com.mhd_07.courtly.core.domain.repo.CoreRepository

class UnfollowUseCase(private val repository: CoreRepository) {
    suspend operator fun invoke(id: String) = repository.unfollow(id)
}