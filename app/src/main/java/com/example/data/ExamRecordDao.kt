package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamRecordDao {
    @Query("SELECT * FROM exam_records ORDER BY timestamp DESC")
    fun getAllExamRecords(): Flow<List<ExamRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExamRecord(record: ExamRecord)

    @Query("SELECT * FROM exam_records WHERE isSynced = 0")
    suspend fun getUnsyncedRecords(): List<ExamRecord>

    @Query("UPDATE exam_records SET isSynced = 1 WHERE id = :id")
    suspend fun markAsSynced(id: Int)
}
