package com.mhd_07.courtly.feature_sign.data.repo

import com.mhd_07.courtly.feature_sign.domain.repo.SignRepository
import androidx.compose.runtime.Composable
import com.mhd_07.courtly.feature_sign.presentation.module.SignResult
import com.mhd_07.courtly.feature_sign.presentation.module.getError
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.compose.auth.composable.NativeSignInResult
import io.github.jan.supabase.compose.auth.composable.NativeSignInState
import io.github.jan.supabase.compose.auth.composable.rememberSignInWithGoogle
import io.github.jan.supabase.compose.auth.composeAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SignRepositoryImpl(private val client: SupabaseClient) : SignRepository {
    @Composable
    override fun signWithGoogle(onResult: (NativeSignInResult) -> Unit): NativeSignInState =
        client.composeAuth.rememberSignInWithGoogle()

    override suspend fun register(email: String, password: String) {
        client.auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }
    }

    override suspend fun login(email: String, password: String) {
        client.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    override suspend fun logout() {
            client.auth.signOut()
    }
}