package com.mhd_07.courtly.feature_sign.domain.usecase

import com.mhd_07.courtly.feature_sign.domain.repo.SignRepository

class VerifyEmailUseCase(private val repository: SignRepository) {
    suspend operator fun invoke(email: String, otp: String) = repository.verifyOtp(email, otp)
}