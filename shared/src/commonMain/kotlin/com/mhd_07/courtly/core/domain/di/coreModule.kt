package com.mhd_07.courtly.core.domain.di

import com.mhd_07.courtly.core.data.repo.CoreRepositoryImpl
import com.mhd_07.courtly.core.domain.repo.CoreRepository
import com.mhd_07.courtly.core.domain.usecase.GetProfileUseCase
import com.mhd_07.courtly.core.domain.usecase.LogoutUseCase
import com.mhd_07.courtly.core.presentation.viewmodel.CoreViewmodel
import com.mhd_07.courtly.shared.BuildKonfig
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.compose.auth.ComposeAuth
import io.github.jan.supabase.compose.auth.googleNativeLogin
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val coreModule = module {
    single {
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
        }
    }
    single<CoreRepository> { CoreRepositoryImpl(get()) }
    single { LogoutUseCase(get()) }
    single { GetProfileUseCase(get()) }
    viewModel<CoreViewmodel>{ CoreViewmodel(get(), get()) }
}