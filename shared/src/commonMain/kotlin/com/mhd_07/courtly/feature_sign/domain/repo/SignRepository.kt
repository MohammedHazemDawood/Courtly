package com.mhd_07.courtly.feature_sign.domain.repo

import androidx.compose.runtime.Composable
import com.mhd_07.courtly.feature_sign.presentation.module.SignResult
import io.github.jan.supabase.compose.auth.composable.NativeSignInResult
import io.github.jan.supabase.compose.auth.composable.NativeSignInState
import kotlinx.coroutines.flow.Flow

interface SignRepository {

    @Composable
    fun signWithGoogle(onResult : (NativeSignInResult) -> Unit) : NativeSignInState

    suspend fun register(email: String, password: String)
    suspend fun login(email: String, password: String)

    suspend fun logout()

}