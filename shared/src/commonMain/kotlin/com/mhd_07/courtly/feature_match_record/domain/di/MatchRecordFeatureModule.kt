package com.mhd_07.courtly.feature_match_record.domain.di

import com.mhd_07.courtly.feature_match_record.presentation.viewmodel.MatchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val matchRecordModule = module {
    viewModel { MatchViewModel() }
}