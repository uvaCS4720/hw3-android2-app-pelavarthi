package edu.nd.pmcburne.hwapp.one.data.network

import kotlinx.serialization.Serializable

@Serializable
data class ScoreboardDto(
    val games: List<GameWrapperDto> = emptyList()
)

@Serializable
data class GameWrapperDto(
    val game: GameDto
)

@Serializable
data class GameDto(
    val gameID: String,
    val gameState: String,          // "pre" | "live" | "final"
    val startTime: String? = null,  // "6:00 PM ET"
    val currentPeriod: String? = null,
    val contestClock: String? = null,
    val away: TeamSideDto,
    val home: TeamSideDto
)

@Serializable
data class TeamSideDto(
    val score: String? = null,
    val winner: Boolean? = null,
    val names: TeamNamesDto
)

@Serializable
data class TeamNamesDto(
    val short: String
)