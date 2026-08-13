package com.mhd_07.courtly.feature_match_record.domain.repo

import com.mhd_07.courtly.core.domain.model.Player

interface MatchSetupRepository {
    suspend fun searchUser(query: String): List<Player>
}