package com.mhd_07.courtly.feature_match_setup.domain.model

sealed interface SetupStep {
    data object Teams : SetupStep

    data object TeamLeftPlayers : SetupStep
    data object TeamRightPlayers : SetupStep
    data object Location : SetupStep
    data object ModeAndType : SetupStep
    data object System: SetupStep
}