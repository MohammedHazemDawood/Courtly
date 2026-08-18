package com.mhd_07.courtly.core.data.mapper

import com.mhd_07.courtly.core.data.model.PlayerResponse
import com.mhd_07.courtly.core.domain.model.Player

fun PlayerResponse.toPlayer() = Player(
    id = id,
    handle = handle ?: "",
    name = display_name ?: "",
    avatar = avatar_path,
    bio = bio ?: "",
    avatarVersion = avatar_version,
    visibility = visibility,
    location = location ?: "",
    cover = cover,
    coverVersion = cover_v
)