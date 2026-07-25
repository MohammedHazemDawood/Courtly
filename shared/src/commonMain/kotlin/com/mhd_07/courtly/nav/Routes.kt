package com.mhd_07.courtly.nav

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Routes : NavKey {
    @Serializable
    data object Home : Routes, NavKey
    @Serializable
    data object GameSetup : Routes, NavKey
    @Serializable
    data object GameRecord : Routes, NavKey
}