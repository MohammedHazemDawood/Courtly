package com.mhd_07.courtly.feature_sign.domain.usecase

import androidx.compose.runtime.Composable
import com.mhd_07.courtly.feature_sign.domain.repo.SignRepository
import io.github.jan.supabase.compose.auth.composable.NativeSignInResult
import io.github.jan.supabase.compose.auth.composable.NativeSignInState

actual class NativeSignInUseCase actual constructor(private val repository: SignRepository) {
    @Composable
    actual operator fun invoke(onResult: (NativeSignInResult) -> Unit) : NativeSignInState{
        return repository.signWithGoogle(onResult)
    }
}