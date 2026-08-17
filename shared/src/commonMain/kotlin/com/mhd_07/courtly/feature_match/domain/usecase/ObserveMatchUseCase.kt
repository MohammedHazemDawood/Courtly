package com.mhd_07.courtly.feature_match.domain.usecase

import com.mhd_07.courtly.feature_match.domain.repository.MatchRepository

class ObserveMatchUseCase(private val repo: MatchRepository) {
    operator fun invoke(id: String) = repo.observeMatch(id)
}