package com.mhd_07.courtly.feature_match_setup.domain.usecase

import com.mhd_07.courtly.feature_match_setup.domain.repository.MatchSetupRepository

class SearchPlayerUseCase(private val repository: MatchSetupRepository) {
    suspend operator fun invoke(query: String) = repository.searchUser(query)
}