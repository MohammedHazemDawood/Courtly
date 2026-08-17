package com.mhd_07.courtly.feature_match.domain.model

import com.mhd_07.courtly.core.domain.model.Player

data class Team(
    val name: String = "",
    val players: List<Player> = emptyList()
) {
    companion object {
        val dummy = Team(
            name = "Team 1",
            players = listOf(
                Player(handle = "@alex", name = "Alex Carter", avatar = null, bio = "Creative playmaker.", avatarVersion = 1),
                Player(handle = "@milo", name = "Milo James", avatar = null, bio = "Fast on the wings.", avatarVersion = 1),
                Player(handle = "@noah", name = "Noah Brooks", avatar = null, bio = "Steady defender.", avatarVersion = 1),
                Player(handle = "@leo", name = "Leo Hall", avatar = null, bio = "Power server.", avatarVersion = 1),
            )
        )

        val dummy2 = Team(
            name = "Team 2",
            players = listOf(
                Player(handle = "@zoe", name = "Zoe Brooks", avatar = null, bio = "Strong all-rounder.", avatarVersion = 1),
                Player(handle = "@ivy", name = "Ivy Chen", avatar = null, bio = "Sharp setter.", avatarVersion = 1),
                Player(handle = "@ryan", name = "Ryan Stone", avatar = null, bio = "Aggressive blocker.", avatarVersion = 1),
                Player(handle = "@sam", name = "Sam Patel", avatar = null, bio = "Reliable finisher.", avatarVersion = 1),
            )
        )
    }
}