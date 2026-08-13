package com.mhd_07.courtly.feature_match_record.domain.di

import com.mhd_07.courtly.feature_match_record.data.repo.MatchSetupRepositoryImpl
import com.mhd_07.courtly.feature_match_record.domain.repo.MatchSetupRepository
import com.mhd_07.courtly.feature_match_record.domain.usecase.SearchUserUseCase
import com.mhd_07.courtly.feature_match_record.presentation.viewmodel.MatchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val matchRecordModule = module {
    single<MatchSetupRepository> { MatchSetupRepositoryImpl(get()) }
    single { SearchUserUseCase(get()) }
    viewModel { MatchViewModel(get()) }
}