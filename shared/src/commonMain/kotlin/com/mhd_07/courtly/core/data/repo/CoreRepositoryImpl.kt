package com.mhd_07.courtly.core.data.repo

import com.mhd_07.courtly.core.data.mapper.toPlayer
import com.mhd_07.courtly.core.data.model.CheckHandle
import com.mhd_07.courtly.core.data.model.CheckHandleRequest
import com.mhd_07.courtly.core.data.model.CheckHandleResponse
import com.mhd_07.courtly.core.data.model.MATCH_FEED
import com.mhd_07.courtly.core.data.model.PROFILES
import com.mhd_07.courtly.core.data.model.PlayerResponse
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.domain.repo.CoreRepository
import com.mhd_07.courtly.feature_match.data.mapper.toMatch
import com.mhd_07.courtly.feature_match.data.model.RemoteMatch
import com.mhd_07.courtly.feature_match.domain.model.Match
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.functions.functions
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import io.ktor.client.call.body
import io.ktor.http.ContentType

class CoreRepositoryImpl(private val client: SupabaseClient) : CoreRepository {

    override suspend fun getProfile(): Player? {
        return client.auth.currentUserOrNull()?.let {
            return client.postgrest.from(PROFILES).select {
                filter {
                    PlayerResponse::id eq it.id
                }
            }.decodeSingle<PlayerResponse>().toPlayer()
        }
    }

    override suspend fun loadMatches(
        page: Long,
        size: Long
    ): List<Match> {
        val from = page * size
        val to = from + size - 1

        return client.postgrest.from(MATCH_FEED).select{
            range(from, to)
        }.decodeList<RemoteMatch>().map { it.toMatch() }
    }

}