package com.mhd_07.courtly.core.domain.usecase

import com.mhd_07.courtly.core.domain.repo.CoreRepository

class CheckHandleUseCase(private val repository: CoreRepository) {
    suspend operator fun invoke(handle: String): Boolean {
        return repository.checkHandle(handle)
    }
}