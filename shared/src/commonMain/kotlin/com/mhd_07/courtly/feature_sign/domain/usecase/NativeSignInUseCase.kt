package com.mhd_07.courtly.feature_sign.domain.usecase

import androidx.compose.runtime.Composable
import com.mhd_07.courtly.feature_sign.domain.repo.SignRepository
import io.github.jan.supabase.compose.auth.composable.NativeSignInResult
import io.github.jan.supabase.compose.auth.composable.NativeSignInState

expect class NativeSignInUseCase(repository: SignRepository) {
    @Composable
    operator fun invoke(onResult: (NativeSignInResult) -> Unit) : NativeSignInState
}
