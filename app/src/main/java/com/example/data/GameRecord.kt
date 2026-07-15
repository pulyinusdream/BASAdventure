package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_records")
data class GameRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val gameName: String, 
    val score: Int,
    val totalItems: Int,
    val starsEarned: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false
)
