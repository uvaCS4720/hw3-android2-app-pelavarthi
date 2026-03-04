package edu.nd.pmcburne.hwapp.one.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey val gameId: String,     // gameID from API
    val dateIso: String,                // "2026-02-17"
    val gender: String,                 // "men" or "women"

    val homeTeam: String,
    val awayTeam: String,

    val homeScore: Int?,
    val awayScore: Int?,

    val status: String,                 // "UPCOMING" | "IN_PROGRESS" | "FINAL"
    val startTime: String?,             // if upcoming
    val period: String?,                // if live
    val clock: String?,                 // if live; "Final" if final
    val winner: String?                 // if final
)