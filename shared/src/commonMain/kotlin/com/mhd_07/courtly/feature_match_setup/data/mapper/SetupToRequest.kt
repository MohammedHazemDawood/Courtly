package com.mhd_07.courtly.feature_match_setup.data.mapper

import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.feature_match_setup.data.model.SetupRequest
import com.mhd_07.courtly.feature_match_setup.domain.model.Setup

fun Setup.toRequest(hostId: String): SetupRequest = SetupRequest(
    created_at = createdAt,
    host = hostId,
    team_left_name = teamLeft.name,
    team_right_name = teamRight.name,
    team_left_players = teamLeft.players.map(Player::id),
    team_right_players = teamRight.players.map(Player::id),
    location = location,
    type = type,
    status = status,
    mode = mode,
    best_of = bestOf,
    ball_half = ballHalf
)