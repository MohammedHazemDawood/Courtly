package com.mhd_07.courtly.feature_match_record.domain.usecase

import com.mhd_07.courtly.feature_match_record.domain.repo.MatchRepository

class SearchUserUseCase(private val repository: MatchRepository) {
    suspend operator fun invoke(query: String) = repository.searchUser(query)
}