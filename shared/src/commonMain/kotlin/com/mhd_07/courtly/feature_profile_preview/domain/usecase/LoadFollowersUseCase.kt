package com.mhd_07.courtly.feature_profile_preview.domain.usecase

import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.domain.repo.CoreRepository
import com.mhd_07.courtly.feature_profile_preview.domain.repository.ProfilePreviewRepository

class LoadFollowersUseCase(private val repository: ProfilePreviewRepository) {
    suspend operator fun invoke(id: String) = repository.loadFollowers(id)
}