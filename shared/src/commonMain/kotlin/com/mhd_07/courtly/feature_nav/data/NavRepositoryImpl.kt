package com.mhd_07.courtly.feature_nav.data

import com.mhd_07.courtly.feature_nav.domain.repo.NavRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.StateFlow

class NavRepositoryImpl(client: SupabaseClient) : NavRepository {
    override val sessionStatus: StateFlow<SessionStatus> = client.auth.sessionStatus
}