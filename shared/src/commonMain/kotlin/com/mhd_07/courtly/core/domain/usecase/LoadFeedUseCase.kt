package com.mhd_07.courtly.core.domain.usecase

import com.mhd_07.courtly.core.domain.repo.CoreRepository

class LoadFeedUseCase(private val repo : CoreRepository) {
    suspend operator fun invoke(page : Long, size : Long = 20) = repo.loadMatches(page, size)
}