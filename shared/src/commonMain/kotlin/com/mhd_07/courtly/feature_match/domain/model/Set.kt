package com.mhd_07.courtly.feature_match.domain.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class Set(
    val team1Games: Int = 0,

    val team2Games: Int = 0,

    val currentGame: Game = Game(),

    val games: PersistentList<Game> = persistentListOf()
)