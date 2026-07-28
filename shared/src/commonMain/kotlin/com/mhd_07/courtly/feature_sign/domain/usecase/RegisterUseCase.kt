package com.mhd_07.courtly.feature_sign.domain.usecase

import com.mhd_07.courtly.feature_sign.domain.repo.SignRepository

data class RegisterUseCase(val repo: SignRepository) {
    suspend operator fun invoke(email: String, password: String) =
        repo.register(email, password)
}