package com.mhd_07.courtly.core.domain.usecase

import com.mhd_07.courtly.core.domain.repo.CoreRepository

class FollowUseCase(private val repository: CoreRepository) {
    suspend operator fun invoke(id: String) = repository.follow(id)
}