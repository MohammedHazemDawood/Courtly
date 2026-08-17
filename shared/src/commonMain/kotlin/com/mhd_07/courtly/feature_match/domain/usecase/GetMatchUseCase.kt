package com.mhd_07.courtly.feature_match.domain.usecase

import com.mhd_07.courtly.feature_match.domain.repository.MatchRepository

class GetMatchUseCase(private val repo: MatchRepository) {
    suspend operator fun invoke(id: String) = repo.getMatch(id)
}