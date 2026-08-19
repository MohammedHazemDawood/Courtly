package com.mhd_07.courtly.feature_profile.presentation.viewmodel.model

import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.presentation.model.RemoteResult

data class ProfileEditState (
    val profile: Player? = null,
    val result: RemoteResult? = null,
    val myId : String? = null,
    val mine : Boolean = false,
    val avatarPath : String? = null,
    val avatarVersion : Int = 0,
    val cover : String? = null,
    val coverVersion : Int = 0,
    val handle : String = "",
    val displayName : String = "",
    val bio : String = "",
    val handleAvailable : Boolean = true,
    val saveEnabled : Boolean = false,
)