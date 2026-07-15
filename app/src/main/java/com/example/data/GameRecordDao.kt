package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameRecordDao {
    @Query("SELECT * FROM game_records ORDER BY timestamp DESC")
    fun getAllGameRecords(): Flow<List<GameRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameRecord(record: GameRecord)

    @Query("SELECT * FROM game_records WHERE isSynced = 0 ORDER BY timestamp ASC")
    suspend fun getUnsyncedRecords(): List<GameRecord>
}
