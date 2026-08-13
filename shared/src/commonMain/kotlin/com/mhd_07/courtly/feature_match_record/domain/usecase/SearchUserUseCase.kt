package com.mhd_07.courtly.feature_match_record.domain.usecase

import com.mhd_07.courtly.feature_match_record.domain.repo.MatchSetupRepository

class SearchUserUseCase(private val repository: MatchSetupRepository) {
    suspend operator fun invoke(query: String) = repository.searchUser(query)
}