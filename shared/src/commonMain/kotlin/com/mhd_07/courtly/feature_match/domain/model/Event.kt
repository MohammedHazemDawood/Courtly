package com.mhd_07.courtly.feature_match.domain.model

import com.mhd_07.courtly.core.domain.model.Player
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.Instant

@Serializable
sealed interface Event {
    val createdAt: Instant

    @Serializable
    data class Start(
        override val createdAt: Instant = Clock.System.now(),
    ) : Event

    @Serializable
    data class Team1Point(
        override val createdAt: Instant = Clock.System.now(),
        val player: Player?,
        var snapshot: Game? = null
    ) : Event

    @Serializable
    data class Team2Point(
        override val createdAt: Instant = Clock.System.now(),
        val player: Player?,
        var snapshot: Game? = null
    ) : Event

    @Serializable
    data class Team1GameWin(
        override val createdAt: Instant = Clock.System.now(),
        var team1Games: Int? = null,
        var team2Games: Int? = null,
    ) : Event

    @Serializable
    data class Team2GameWin(
        override val createdAt: Instant = Clock.System.now(),
        var team1Games: Int? = null,
        var team2Games: Int? = null,
    ) : Event

    @Serializable
    data class Team1SetWin(
        override val createdAt: Instant = Clock.System.now(),
        var team1Games: Int? = null,
        var team2Games: Int? = null,
        var team1Sets: Int? = null,
        var team2Sets: Int? = null,
    ) : Event

    @Serializable
    data class Team2SetWin(
        override val createdAt: Instant = Clock.System.now(),
        var team1Games: Int? = null,
        var team2Games: Int? = null,
        var team1Sets: Int? = null,
        var team2Sets: Int? = null,
    ) : Event

    @Serializable
    data class Team1Won(override val createdAt: Instant = Clock.System.now()) : Event

    @Serializable
    data class Team2Won(override val createdAt: Instant = Clock.System.now()) : Event

    @Serializable
    data class Done(override val createdAt: Instant = Clock.System.now()) : Event

}
