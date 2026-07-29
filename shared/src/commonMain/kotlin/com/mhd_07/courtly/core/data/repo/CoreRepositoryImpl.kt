package com.mhd_07.courtly.core.data.repo

import com.mhd_07.courtly.core.domain.repo.CoreRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth

class CoreRepositoryImpl(private val client : SupabaseClient) : CoreRepository {
    override suspend fun logout() {
        client.auth.signOut()
    }
}