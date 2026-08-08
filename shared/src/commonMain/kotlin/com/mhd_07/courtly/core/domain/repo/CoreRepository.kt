package com.mhd_07.courtly.core.domain.repo

import com.mhd_07.courtly.core.domain.model.Profile

interface CoreRepository {
    suspend fun logout()
    suspend fun getProfile() : Profile?
}