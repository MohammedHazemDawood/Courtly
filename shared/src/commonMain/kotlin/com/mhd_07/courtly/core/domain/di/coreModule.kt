package com.mhd_07.courtly.core.domain.di

import com.mhd_07.courtly.core.data.repo.CoreRepositoryImpl
import com.mhd_07.courtly.core.domain.repo.CoreRepository
import com.mhd_07.courtly.core.domain.usecase.CheckHandleUseCase
import com.mhd_07.courtly.core.domain.usecase.GetProfileUseCase
import com.mhd_07.courtly.core.domain.usecase.LogoutUseCase
import com.mhd_07.courtly.core.domain.usecase.UpdateAvatarUseCase
import com.mhd_07.courtly.core.domain.usecase.UpdateProfileUseCase
import com.mhd_07.courtly.core.presentation.viewmodel.CoreViewmodel
import com.mhd_07.courtly.shared.BuildKonfig
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.compose.auth.ComposeAuth
import io.github.jan.supabase.compose.auth.googleNativeLogin
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val coreModule = module {
    single {
        println("========== SUPABASE ==========")
        println("URL: ${BuildKonfig.SUPABASE_URL}")
        println("==============================")
        createSupabaseClient(
            supabaseUrl = BuildKonfig.SUPABASE_URL,
            supabaseKey = BuildKonfig.SUPABASE_KEY
        ) {
            install(Auth)
            install(ComposeAuth) {
                googleNativeLogin(serverClientId = BuildKonfig.GOOGLE_WEB_CLIENT_ID)
            }
            install(Realtime)
            install(Postgrest)
            install(Functions)
            install(Storage)
        }
    }
    single<CoreRepository> { CoreRepositoryImpl(get()) }
    single { LogoutUseCase(get()) }
    single { GetProfileUseCase(get()) }
    single { CheckHandleUseCase(get()) }
    single { UpdateProfileUseCase(get()) }
    single { UpdateAvatarUseCase(get()) }
    viewModel<CoreViewmodel> {
        CoreViewmodel(
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }
}