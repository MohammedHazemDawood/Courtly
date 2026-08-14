package com.mhd_07.courtly.feature_profile_preview.domain.usecase

import com.mhd_07.courtly.feature_profile_preview.domain.repository.ProfilePreviewRepository

class GetUserId(private val repo: ProfilePreviewRepository) {
    operator fun invoke(): String? {
        return repo.getUserId()
    }
}