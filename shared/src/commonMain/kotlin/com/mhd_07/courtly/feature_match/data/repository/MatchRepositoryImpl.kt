package com.mhd_07.courtly.feature_match.data.repository

import com.mhd_07.courtly.core.data.model.MATCHES
import com.mhd_07.courtly.feature_match.data.mapper.toMatch
import com.mhd_07.courtly.feature_match.data.mapper.toRemote
import com.mhd_07.courtly.feature_match.data.model.RemoteMatch
import com.mhd_07.courtly.feature_match.domain.model.Match
import com.mhd_07.courtly.feature_match.domain.repository.MatchRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.decodeRecord
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.postgresSingleDataFlow
import io.github.jan.supabase.realtime.realtime
import io.github.jan.supabase.realtime.selectSingleValueAsFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.subscribe

class MatchRepositoryImpl(private val client: SupabaseClient) : MatchRepository {

    override val userId: String?
        get() = client.auth.currentUserOrNull()?.id

    override suspend fun getMatch(id: String): Match {
        return client.postgrest.from(MATCHES).select {
            filter {
                RemoteMatch::id eq id
            }
        }.decodeSingle<RemoteMatch>().toMatch()
    }

    @OptIn(SupabaseExperimental::class)
    override fun observeMatch(id: String): Flow<Match> =
        client.from(MATCHES).selectSingleValueAsFlow(RemoteMatch::id) {
            RemoteMatch::id eq id
        }.map {
            it.toMatch()
        }


    override suspend fun updateMatch(match: Match) {
        val currentUser = client.auth.currentUserOrNull()?.also {
            println("UserID : ${it.id}")
            println("Match Host : ${match.hostId}")
        }
            ?: throw IllegalStateException("User is not authenticated")
        if (currentUser.id != match.hostId) {
            throw IllegalStateException("User is not the host of the match")
        }

        client.postgrest.from(MATCHES).update(match.toRemote()) {
            filter {
                RemoteMatch::id eq match.id
            }
        }
    }
}