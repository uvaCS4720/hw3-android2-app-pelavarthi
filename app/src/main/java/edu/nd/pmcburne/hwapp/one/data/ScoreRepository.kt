package edu.nd.pmcburne.hwapp.one.data

import edu.nd.pmcburne.hwapp.one.data.db.GameDao
import edu.nd.pmcburne.hwapp.one.data.db.GameEntity
import edu.nd.pmcburne.hwapp.one.data.network.NcaaApi
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class ScoreRepository(
    private val api: NcaaApi,
    private val dao: GameDao
) {
    fun observeGames(date: LocalDate, gender: String): Flow<List<GameEntity>> =
        dao.observeGames(date.toString(), gender)

    suspend fun refresh(date: LocalDate, gender: String) {
        val yyyy = date.year.toString()
        val mm = date.monthValue.toString().padStart(2, '0')
        val dd = date.dayOfMonth.toString().padStart(2, '0')

        val dto = api.getScoreboard(gender, yyyy, mm, dd)

        val entities = dto.games.map { wrapper ->
            val g = wrapper.game

            val status = when (g.gameState.lowercase()) {
                "pre" -> "UPCOMING"
                "live" -> "IN_PROGRESS"
                "final" -> "FINAL"
                else -> "UPCOMING"
            }

            val winner = when {
                g.home.winner == true -> g.home.names.short
                g.away.winner == true -> g.away.names.short
                else -> null
            }

            GameEntity(
                gameId = g.gameID,
                dateIso = date.toString(),
                gender = gender,
                homeTeam = g.home.names.short,
                awayTeam = g.away.names.short,
                homeScore = g.home.score?.toIntOrNull(),
                awayScore = g.away.score?.toIntOrNull(),
                status = status,
                startTime = if (status == "UPCOMING") g.startTime else null,
                period = if (status == "IN_PROGRESS") g.currentPeriod else null,
                clock = when (status) {
                    "FINAL" -> "Final"
                    "IN_PROGRESS" -> g.contestClock
                    else -> null
                },
                winner = if (status == "FINAL") winner else null
            )
        }

        dao.upsertAll(entities)
    }
}