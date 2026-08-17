package com.mhd_07.courtly.feature_match.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhd_07.courtly.feature_match.domain.usecase.GetMatchUseCase
import com.mhd_07.courtly.feature_profile_preview.domain.usecase.GetUserId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MatchControllerViewmodel(
    getUserId: GetUserId,
    getMatch: GetMatchUseCase,
    matchId: String
) : ViewModel() {
    private val userId = getUserId()
    val isMine = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            val match = getMatch(matchId)
            isMine.value = match.hostId == userId
        }
    }
}