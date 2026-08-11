package com.mhd_07.courtly.feature_match_record.domain.di

import com.mhd_07.courtly.feature_match_record.data.repo.MatchRepositoryImpl
import com.mhd_07.courtly.feature_match_record.domain.repo.MatchRepository
import com.mhd_07.courtly.feature_match_record.domain.usecase.SearchUserUseCase
import com.mhd_07.courtly.feature_match_record.presentation.viewmodel.MatchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val matchRecordModule = module {
    single<MatchRepository> { MatchRepositoryImpl(get()) }
    single { SearchUserUseCase(get()) }
    viewModel { MatchViewModel(get()) }
}