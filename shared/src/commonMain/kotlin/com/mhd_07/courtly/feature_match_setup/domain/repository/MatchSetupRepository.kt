package com.mhd_07.courtly.feature_match_setup.domain.repository

import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.feature_match_setup.domain.model.Setup

interface MatchSetupRepository {
    suspend fun setupMatch(setup: Setup) : String?
    suspend fun searchUser(query: String) : List<Player>
}