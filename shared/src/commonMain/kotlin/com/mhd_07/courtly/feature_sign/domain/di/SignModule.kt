package com.mhd_07.courtly.feature_sign.domain.di

import com.mhd_07.courtly.feature_sign.presentation.viewmodel.SignViewmodel
import com.mhd_07.courtly.feature_sign.data.repo.SignRepositoryImpl
import com.mhd_07.courtly.feature_sign.domain.repo.SignRepository
import com.mhd_07.courtly.feature_sign.domain.usecase.LogOutUseCase
import com.mhd_07.courtly.feature_sign.domain.usecase.LoginUseCase
import com.mhd_07.courtly.feature_sign.domain.usecase.RegisterUseCase
import com.mhd_07.courtly.feature_sign.domain.usecase.SignWithGoogleUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val signModule = module {
    single<SignRepository> { SignRepositoryImpl(get()) }
    single { LoginUseCase(get()) }
    single { RegisterUseCase(get()) }
    single{ LogOutUseCase(get()) }
    single { SignWithGoogleUseCase(get()) }
    viewModel<SignViewmodel> { SignViewmodel(get(), get(), get(), get()) }
}