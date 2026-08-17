package com.mhd_07.courtly.feature_match.domain.di

import com.mhd_07.courtly.feature_match.data.repository.MatchRepositoryImpl
import com.mhd_07.courtly.feature_match.domain.repository.MatchRepository
import com.mhd_07.courtly.feature_match.domain.usecase.GetMatchUseCase
import com.mhd_07.courtly.feature_match.domain.usecase.GetUserIdUseCase
import com.mhd_07.courtly.feature_match.domain.usecase.ObserveMatchUseCase
import com.mhd_07.courtly.feature_match.domain.usecase.UpdateMatchUseCase
import com.mhd_07.courtly.feature_match.presentation.viewmodel.MatchControllerViewmodel
import com.mhd_07.courtly.feature_match.presentation.viewmodel.MatchPreviewViewmodel
import com.mhd_07.courtly.feature_match.presentation.viewmodel.MatchRecordViewmodel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val matchModule = module {
    single<MatchRepository> { MatchRepositoryImpl(get()) }
    single { UpdateMatchUseCase(get()) }
    single { GetMatchUseCase(get()) }
    single { ObserveMatchUseCase(get()) }
    single { GetUserIdUseCase(get()) }

    viewModel { (id: String) -> MatchControllerViewmodel(get(), get(), id) }
    viewModel { (id: String) -> MatchRecordViewmodel(get(), get(), id) }
    viewModel { (id: String) -> MatchPreviewViewmodel(get(),  id) }
}