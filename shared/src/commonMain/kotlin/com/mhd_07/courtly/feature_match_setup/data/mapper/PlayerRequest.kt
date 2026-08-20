package com.mhd_07.courtly.feature_match_setup.data.mapper

import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.feature_match_setup.data.model.PlayerRequest

fun Player.toRequest() = PlayerRequest(
    id = id,
    is_remote = isRemote,
    display_name = name
)