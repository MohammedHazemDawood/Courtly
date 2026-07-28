package com.mhd_07.courtly.feature_nav.presentation.data

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Graphs : NavKey {

    @Serializable
    data object Sign : Graphs, NavKey {
        @Serializable
        data object SignOptions : Graphs, NavKey
        @Serializable
        data object MailPasswordSign : Graphs, NavKey
    }

    @Serializable
    data object Core : Graphs, NavKey {
        @Serializable
        data object Home : Graphs, NavKey

        @Serializable
        data object Settings : Graphs, NavKey
    }

    @Serializable
    data object Match : Graphs, NavKey {
        @Serializable
        data object Setup : Graphs, NavKey

        @Serializable
        data object Record : Graphs, NavKey
    }
}