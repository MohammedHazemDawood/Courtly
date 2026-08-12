package com.mhd_07.courtly.feature_sign.domain.di

import com.mhd_07.courtly.feature_sign.presentation.viewmodel.SignViewmodel
import com.mhd_07.courtly.feature_sign.data.repo.SignRepositoryImpl
import com.mhd_07.courtly.feature_sign.domain.repo.SignRepository
import com.mhd_07.courtly.feature_sign.domain.usecase.CheckEmailUseCase
import com.mhd_07.courtly.feature_sign.domain.usecase.LoginUseCase
import com.mhd_07.courtly.feature_sign.domain.usecase.RegisterUseCase
import com.mhd_07.courtly.feature_sign.domain.usecase.ResendEmailUseCase
import com.mhd_07.courtly.feature_sign.domain.usecase.NativeSignInUseCase
import com.mhd_07.courtly.feature_sign.domain.usecase.VerifyEmailUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val signModule = module {
    single<SignRepository> { SignRepositoryImpl(get()) }
    single { LoginUseCase(get()) }
    single { RegisterUseCase(get()) }
    single { NativeSignInUseCase(get()) }
    single { CheckEmailUseCase(get()) }
    single { ResendEmailUseCase(get()) }
    single { VerifyEmailUseCase(get()) }
    viewModel<SignViewmodel> { SignViewmodel(get(), get(), get(), get(), get(), get()) }
}