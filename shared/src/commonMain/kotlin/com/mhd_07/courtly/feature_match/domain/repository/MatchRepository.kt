package com.mhd_07.courtly.feature_match.domain.repository

import com.mhd_07.courtly.feature_match.domain.model.Match
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface MatchRepository {
    val userId: String?
    suspend fun getMatch(id: String): Match
    fun observeMatch(id: String): Flow<Match>
    suspend fun updateMatch(match: Match)
}