package com.mhd_07.courtly.feature_match.data.mapper

import com.mhd_07.courtly.feature_match.data.model.RemoteMatch
import com.mhd_07.courtly.feature_match.domain.model.Match
import com.mhd_07.courtly.feature_match.domain.model.Rules
import com.mhd_07.courtly.feature_match.domain.model.Team
import kotlinx.collections.immutable.toPersistentList

fun RemoteMatch.toMatch() = Match(
    createdAt = created_at,
    startedAt = started_at,
    doneAt = done_at,
    hostId = host,
    team1 = Team(name = team_1_name, players = team_1_players.map {
        println("players: $it")
        it.toPlayer()
    }),
    team2 = Team(name = team_2_name, players = team_2_players.map {
        println("players: $it")
        it.toPlayer()
    }),
    team1Sets = team_1_sets,
    team2Sets = team_2_sets,
    sets = sets.map { it.toSet() }.toPersistentList(),
    rules = Rules(bestOf = best_of, type = type, mode = mode),
    winner = winner,
    status = status,
    currentSetIndex = current_set_index,
    currentServeSide = current_serve_side,
    currentCourtSide = current_court_side,
    timeLine = timeline.toPersistentList(),
    id = id
)

fun Match.toRemote(): RemoteMatch = RemoteMatch(
    id = id,
    created_at = createdAt,
    started_at = startedAt,
    host = hostId,
    team_1_name = team1.name,
    team_2_name = team2.name,
    team_1_players = team1.players.map {
        println("players: $it")
        it.toRemote()
    },
    team_2_players = team2.players.map {
        println("players: $it")
        it.toRemote()
    },
    team_1_sets = team1Sets,
    team_2_sets = team2Sets,
    current_set_index = currentSetIndex,
    current_serve_side = currentServeSide,
    current_court_side = currentCourtSide,
    status = status,
    winner = winner,
    type = rules.type,
    mode = rules.mode,
    best_of = rules.bestOf,
    sets = sets.map { it.toRemote() },
    timeline = timeLine,
    done_at = doneAt
)