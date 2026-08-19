package com.mhd_07.courtly.feature_match_setup.data.mapper

import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.feature_match.data.mapper.toRemote
import com.mhd_07.courtly.feature_match.domain.model.Set
import com.mhd_07.courtly.feature_match_setup.data.model.SetupRequest
import com.mhd_07.courtly.feature_match_setup.domain.model.Setup

fun Setup.toRequest(hostId: String): SetupRequest = SetupRequest(
    created_at = createdAt,
    host = hostId,
    team_1_name = teamLeft.name,
    team_2_name = teamRight.name,
    team_1_players = teamLeft.players.map(Player::toRemote),
    team_2_players = teamRight.players.map(Player::toRemote),
    location = location,
    type = type,
    status = status,
    mode = mode,
    best_of = bestOf,
    sets = listOf(Set().toRemote())
//    ball_half = ballHalf
)