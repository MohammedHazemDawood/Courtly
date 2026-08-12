package com.mhd_07.courtly.core.domain.usecase

import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.domain.repo.CoreRepository

class LoadFollowersUseCase(private val repository: CoreRepository) {
    suspend operator fun invoke() : List<Player> = repository.loadFollowers()
}