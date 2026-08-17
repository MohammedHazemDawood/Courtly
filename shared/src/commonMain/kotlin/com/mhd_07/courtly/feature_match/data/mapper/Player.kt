package com.mhd_07.courtly.feature_match.data.mapper

import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.domain.model.Visibility
import com.mhd_07.courtly.feature_match.data.model.RemotePlayer

fun Player.toRemote() = RemotePlayer(
    id = id,
    handle = handle,
    name = name,
    avatar = avatar,
    avatar_version = avatarVersion,
    bench = bench,
    is_remote = isRemote
)

fun RemotePlayer.toPlayer() = Player(
    id = id,
    handle = handle,
    name = name,
    avatar = avatar,
    bio = "",
    avatarVersion = avatar_version,
    bench = bench,
    visibility = Visibility.Public,
    location = "",
    isRemote = is_remote
)