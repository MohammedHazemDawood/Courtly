package com.mhd_07.courtly.feature_profile.domain.di

import com.mhd_07.courtly.feature_profile.data.repository.ProfileRepositoryImpl
import com.mhd_07.courtly.feature_profile.domain.repository.ProfileRepository
import org.koin.dsl.module
import com.mhd_07.courtly.feature_profile.domain.usecase.*
import com.mhd_07.courtly.feature_profile.presentation.viewmodel.ProfileEditViewmodel
import com.mhd_07.courtly.feature_profile.presentation.viewmodel.ProfilePreviewViewModel
import org.koin.core.module.dsl.viewModel

val profilePreviewModule = module {
    single<ProfileRepository> { ProfileRepositoryImpl(get()) }
    single { GetProfileByIdUseCase(get()) }
    single { LoadFollowersUseCase(get()) }
    single { LoadFollowingUseCase(get()) }
    single { FollowUseCase(get()) }
    single { UnfollowUseCase(get()) }
    single { GetUserId(get()) }
    single { GetProfileByHandleUseCase(get()) }
    single { LoadMatchesUseCase(get()) }
    single { CheckHandleUseCase(get()) }
    single { UpdateProfileUseCase(get()) }
    single { UpdateAvatarUseCase(get()) }
    single { UpdateCoverUseCase(get()) }
    single { LogoutUseCase(get()) }
    single { GetUserProfileUseCase(get()) }
    viewModel<ProfilePreviewViewModel> {
        ProfilePreviewViewModel(
            get(), get(), get(), get(), get(), get(), get(), get(), get()
        )
    }
    viewModel {
        ProfileEditViewmodel(get(), get(), get(),
            get(), get())
    }
}