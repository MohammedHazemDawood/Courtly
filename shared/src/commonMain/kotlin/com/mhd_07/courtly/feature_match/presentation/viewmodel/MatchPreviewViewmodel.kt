package com.mhd_07.courtly.feature_match.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhd_07.courtly.feature_match.domain.model.Match
import com.mhd_07.courtly.feature_match.domain.usecase.ObserveMatchUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class MatchPreviewViewmodel(
    observeMatch: ObserveMatchUseCase,
    matchId: String
) : ViewModel() {
    val state = observeMatch(matchId).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        Match.initial
    )
}