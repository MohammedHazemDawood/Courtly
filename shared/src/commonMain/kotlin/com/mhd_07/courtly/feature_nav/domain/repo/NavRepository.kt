package com.mhd_07.courtly.feature_nav.domain.repo

import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.StateFlow

interface NavRepository {
    val sessionStatus: StateFlow<SessionStatus>
}