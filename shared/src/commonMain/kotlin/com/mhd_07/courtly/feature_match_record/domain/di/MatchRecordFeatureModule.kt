package com.mhd_07.courtly.feature_match_record.domain.di

import com.mhd_07.courtly.feature_match_record.presentation.viewmodel.MatchViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val matchRecordModule = module {
    viewModel<MatchViewModel>()
}