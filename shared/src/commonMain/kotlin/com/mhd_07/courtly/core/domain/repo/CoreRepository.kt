package com.mhd_07.courtly.core.domain.repo

import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.feature_match.domain.model.Match

interface CoreRepository {
    suspend fun getProfile() : Player?

    suspend fun loadMatches(page : Long, size : Long) : List<Match>
}