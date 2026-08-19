package com.mhd_07.courtly.feature_profile.domain.usecase

import com.mhd_07.courtly.feature_profile.domain.repository.ProfileRepository

class GetUserId(private val repo: ProfileRepository) {
    operator fun invoke(): String? {
        return repo.getUserId()
    }
}