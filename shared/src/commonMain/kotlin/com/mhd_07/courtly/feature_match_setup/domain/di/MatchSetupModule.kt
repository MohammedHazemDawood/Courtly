package com.mhd_07.courtly.feature_match_setup.domain.di

import com.mhd_07.courtly.feature_match_setup.data.repository.MatchSetupRepositoryImpl
import com.mhd_07.courtly.feature_match_setup.domain.repository.MatchSetupRepository
import com.mhd_07.courtly.feature_match_setup.domain.usecase.*
import com.mhd_07.courtly.feature_match_setup.presentation.viewmodel.MatchSetupViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val matchSetupModule = module {
    single<MatchSetupRepository> { MatchSetupRepositoryImpl(get()) }
    single<SetupUseCase> { SetupUseCase(get()) }
    single<SearchPlayerUseCase> { SearchPlayerUseCase(get()) }
    viewModel<MatchSetupViewModel> { MatchSetupViewModel(get(), get()) }
}