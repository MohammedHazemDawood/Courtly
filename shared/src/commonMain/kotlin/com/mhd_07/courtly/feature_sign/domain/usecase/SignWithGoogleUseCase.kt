package com.mhd_07.courtly.feature_sign.domain.usecase

import androidx.compose.runtime.Composable
import com.mhd_07.courtly.feature_sign.domain.repo.SignRepository
import io.github.jan.supabase.compose.auth.composable.NativeSignInResult

class SignWithGoogleUseCase(private val repository: SignRepository) {
    @Composable
    operator fun invoke(onResult: (NativeSignInResult) -> Unit) =
        repository.signWithGoogle(onResult = onResult)
}
