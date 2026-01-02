package com.example.uas_mocom.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {
    @Query("SELECT * FROM matches ORDER BY timestamp DESC")
    fun observeMatches(): Flow<List<MatchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(match: MatchEntity)

    @Query("DELETE FROM matches")
    suspend fun clearAll()
}
