package com.mhd_07.courtly.feature_match_record.data.repo

import com.mhd_07.courtly.core.data.mapper.toPlayer
import com.mhd_07.courtly.core.data.model.PROFILES
import com.mhd_07.courtly.core.data.model.PlayerResponse
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.feature_match_record.domain.repo.MatchSetupRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest

class MatchSetupRepositoryImpl(private val client: SupabaseClient) : MatchSetupRepository {
    override suspend fun searchUser(query: String): List<Player> {
        val query = query.trim('@', ' ')
        if (query.isEmpty()) return emptyList()

        println("SEARCH QUERY = '$query'")

        val result = client
            .postgrest
            .from(PROFILES)
            .select {
                filter {
                    or {
                        PlayerResponse::handle ilike "$query%"
                        PlayerResponse::display_name ilike "$query%"
                    }
                }
            }
            .decodeList<PlayerResponse>()
            .map(PlayerResponse::toPlayer)

        println("SEARCH RESULT = '$query' -> $result")

        return result
    }
}