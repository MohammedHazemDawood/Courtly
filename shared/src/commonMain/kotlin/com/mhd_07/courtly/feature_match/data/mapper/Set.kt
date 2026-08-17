package com.mhd_07.courtly.feature_match.data.mapper

import com.mhd_07.courtly.feature_match.data.model.RemoteSet
import com.mhd_07.courtly.feature_match.domain.model.Set
import kotlinx.collections.immutable.toPersistentList

fun RemoteSet.toSet() = Set(
    team1Games = team_1_games,
    team2Games = team_2_games,
    currentGame = current_game,
    games = games.toPersistentList()
)

fun Set.toRemote() = RemoteSet(
    team_1_games = team1Games,
    team_2_games = team2Games,
    current_game = currentGame,
    games = games.toList()
)