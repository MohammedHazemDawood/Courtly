package com.mhd_07.courtly.feature_nav.domain.di

import com.mhd_07.courtly.feature_nav.data.NavRepositoryImpl
import com.mhd_07.courtly.feature_nav.domain.repo.NavRepository
import com.mhd_07.courtly.feature_nav.domain.usecase.AuthStatus
import com.mhd_07.courtly.feature_nav.presentation.viemodel.NavViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val navModule = module {
    single<NavRepository> { NavRepositoryImpl(get()) }
    single<AuthStatus> { AuthStatus(get()) }
    viewModel<NavViewModel> { NavViewModel(get()) }
}