package com.mhd_07.courtly.core.data.repo

import com.mhd_07.courtly.core.data.mapper.toProfile
import com.mhd_07.courtly.core.data.model.PROFILES
import com.mhd_07.courtly.core.data.model.ProfileResponse
import com.mhd_07.courtly.core.domain.model.Profile
import com.mhd_07.courtly.core.domain.repo.CoreRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest

class CoreRepositoryImpl(private val client: SupabaseClient) : CoreRepository {
    override suspend fun logout() {
        client.auth.signOut()
    }

    override suspend fun getProfile(): Profile? {
        return client.auth.currentUserOrNull()?.let {
            client.postgrest.from(PROFILES).select {
                filter {
                    ProfileResponse::id eq it.id
                }
            }.decodeSingle<ProfileResponse>().toProfile()
        }
    }
}