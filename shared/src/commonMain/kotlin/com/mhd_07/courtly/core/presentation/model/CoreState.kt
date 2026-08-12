package com.mhd_07.courtly.core.presentation.model

import androidx.navigation3.runtime.NavBackStack
import com.mhd_07.courtly.core.domain.model.Player

data class CoreState(
    val profile: Player? = null,
    val avatarPath : String? = null,
    val avatarVersion : Int = 0,
    val handle : String = "",
    val displayName : String = "",
    val bio : String = "",
    val handleAvailable : Boolean = true,
    val result: RemoteResult? = null,
    val saveEnabled : Boolean = false,
    val following : List<Player> = emptyList(),
    val followers : List<Player> = emptyList()
)
