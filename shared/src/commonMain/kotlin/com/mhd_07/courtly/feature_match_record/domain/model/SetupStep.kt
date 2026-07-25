package com.mhd_07.courtly.feature_match_record.domain.model

sealed interface SetupStep {
    data object TeamLeft : SetupStep
    data object TeamRight : SetupStep
    data object Location : SetupStep
    data object Settings : SetupStep
}