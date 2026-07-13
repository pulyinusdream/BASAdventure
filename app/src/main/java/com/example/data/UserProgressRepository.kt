package com.example.data

import kotlinx.coroutines.flow.Flow

class UserProgressRepository(private val userProgressDao: UserProgressDao) {
    val progress: Flow<UserProgress?> = userProgressDao.getProgress()

    suspend fun saveProgress(progress: UserProgress) {
        userProgressDao.saveProgress(progress)
    }
}
