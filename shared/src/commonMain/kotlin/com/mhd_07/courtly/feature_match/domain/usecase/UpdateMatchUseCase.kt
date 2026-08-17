package com.mhd_07.courtly.feature_match.domain.usecase

import com.mhd_07.courtly.feature_match.domain.model.Match
import com.mhd_07.courtly.feature_match.domain.repository.MatchRepository

class UpdateMatchUseCase(private val repo: MatchRepository) {
    suspend operator fun invoke(match: Match) {
        repo.updateMatch(match)
    }
}