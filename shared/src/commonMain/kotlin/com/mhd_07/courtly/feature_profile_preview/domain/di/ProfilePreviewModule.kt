package com.mhd_07.courtly.feature_profile_preview.domain.di

import com.mhd_07.courtly.feature_profile_preview.data.repository.ProfilePreviewRepositoryImpl
import com.mhd_07.courtly.feature_profile_preview.domain.repository.ProfilePreviewRepository
import org.koin.dsl.module
import com.mhd_07.courtly.feature_profile_preview.domain.usecase.*
import com.mhd_07.courtly.feature_profile_preview.presentation.viewmodel.ProfilePreviewViewModel
import org.koin.core.module.dsl.viewModel

val profilePreviewModule = module {
    single<ProfilePreviewRepository> { ProfilePreviewRepositoryImpl(get()) }
    single { GetProfileUseCase(get()) }
    single { LoadFollowersUseCase(get()) }
    single { LoadFollowingUseCase(get()) }
    single { FollowUseCase(get()) }
    single { UnfollowUseCase(get()) }
    single { GetUserId(get()) }
    viewModel<ProfilePreviewViewModel> { ProfilePreviewViewModel(get(), get(), get(), get(), get(), get()) }
}