package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgress(
    @PrimaryKey val id: Int = 1,
    val stars: Int = 0,
    val masteredLetters: String = "", // Comma-separated list like "A,B,K"
    val selectedAvatar: String = "👦", // Avatars: 👦, 👧, 🦊, 🦁, 🐼
    val soundEnabled: Boolean = true,
    val ttsSpeed: Float = 1.0f, // 0.7f (Mabagal), 1.0f (Normal), 1.3f (Mabilis)
    val gradeLevel: String = "",
    val surname: String = "",
    val firstName: String = "",
    val middleInitial: String = "",
    val gender: String = "",
    val unlockedLevel: Int = 1
)
