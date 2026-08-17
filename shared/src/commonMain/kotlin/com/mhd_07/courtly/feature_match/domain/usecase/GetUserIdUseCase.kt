package com.mhd_07.courtly.feature_match.domain.usecase

import com.mhd_07.courtly.feature_match.domain.repository.MatchRepository

class GetUserIdUseCase(private val repo : MatchRepository) {
    operator fun invoke() = repo.userId
}