package edu.nd.pmcburne.hwapp.one.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Query("""
        SELECT * FROM games
        WHERE dateIso = :dateIso AND gender = :gender
        ORDER BY startTime ASC
    """)
    fun observeGames(dateIso: String, gender: String): Flow<List<GameEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(games: List<GameEntity>)
}