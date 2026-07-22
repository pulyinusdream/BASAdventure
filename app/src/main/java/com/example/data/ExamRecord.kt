package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exam_records")
data class ExamRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val examName: String, // Pagsasanay 1, Pagsasanay 2, Pagsasanay 3
    val score: Int,
    val totalItems: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false
)
