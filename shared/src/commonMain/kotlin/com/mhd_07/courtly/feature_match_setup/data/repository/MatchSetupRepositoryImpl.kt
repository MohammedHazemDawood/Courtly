package com.mhd_07.courtly.feature_match_setup.data.repository

import com.mhd_07.courtly.core.data.mapper.toPlayer
import com.mhd_07.courtly.core.data.model.PROFILES
import com.mhd_07.courtly.core.data.model.PlayerResponse
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.feature_match_record.data.model.MATCHES
import com.mhd_07.courtly.feature_match_setup.data.mapper.toRequest
import com.mhd_07.courtly.feature_match_setup.data.model.SetupResponse
import com.mhd_07.courtly.feature_match_setup.domain.model.Setup
import com.mhd_07.courtly.feature_match_setup.domain.repository.MatchSetupRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns

class MatchSetupRepositoryImpl(private val client: SupabaseClient) : MatchSetupRepository {
    override suspend fun setupMatch(setup: Setup): String? =
        client.auth.currentUserOrNull()?.id?.let {
            client.postgrest.from(MATCHES).insert(setup.toRequest(it)) {
                select(Columns.list("id"))
            }.decodeSingle<SetupResponse>().id
        }
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