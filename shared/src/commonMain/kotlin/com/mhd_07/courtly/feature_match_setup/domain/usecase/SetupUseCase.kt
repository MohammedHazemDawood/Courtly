package com.mhd_07.courtly.feature_match_setup.domain.usecase

import com.mhd_07.courtly.feature_match_setup.domain.model.Setup
import com.mhd_07.courtly.feature_match_setup.domain.repository.MatchSetupRepository

class SetupUseCase(private val repository: MatchSetupRepository) {
    suspend operator fun invoke(setup: Setup) = repository.setupMatch(setup)
}