package com.mhd_07.courtly.feature_sign.data.repo

import com.mhd_07.courtly.feature_sign.domain.repo.SignRepository
import androidx.compose.runtime.Composable
import com.mhd_07.courtly.feature_sign.data.model.Request
import com.mhd_07.courtly.feature_sign.data.model.Response
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.compose.auth.composable.NativeSignInResult
import io.github.jan.supabase.compose.auth.composable.NativeSignInState
import io.github.jan.supabase.compose.auth.composable.rememberSignInWithGoogle
import io.github.jan.supabase.compose.auth.composeAuth
import io.github.jan.supabase.functions.functions
import io.ktor.client.call.body

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

    override suspend fun emailExists(email: String): Boolean {
        val data = client.functions.invoke("check-email", body = Request(email)).body<Response>()
        return data.exists
    }

    override suspend fun resendOtp(email: String) {
        client.auth.resendEmail(email = email, type = OtpType.Email.SIGNUP)
    }

    override suspend fun verifyOtp(email: String, otp: String) {
        client.auth.verifyEmailOtp(type = OtpType.Email.SIGNUP, email = email, token = otp)
    }
}