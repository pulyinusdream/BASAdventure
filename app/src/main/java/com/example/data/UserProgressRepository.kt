package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import android.util.Log
import com.example.BuildConfig
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class UserProgressRepository(private val userProgressDao: UserProgressDao, private val gameRecordDao: GameRecordDao) {
    val progress: Flow<UserProgress?> = userProgressDao.getProgress()
    val allGameRecords: Flow<List<GameRecord>> = gameRecordDao.getAllGameRecords()

    suspend fun saveProgress(progress: UserProgress) {
        // Mark as unsynced whenever we save
        userProgressDao.saveProgress(progress.copy(isSynced = false))
        syncPendingData()
    }

    suspend fun saveGameRecord(record: GameRecord) {
        gameRecordDao.insertGameRecord(record.copy(isSynced = false))
        syncPendingData()
    }

    private suspend fun syncPendingData() {
        if (!BuildConfig.SUPABASE_URL.startsWith("http") || BuildConfig.SUPABASE_ANON_KEY.isEmpty()) {
            return // Skip if no valid config
        }

        try {
            val currentProgress = userProgressDao.getProgressSync()
            if (currentProgress != null && !currentProgress.isSynced) {
                val dto = UserProgressDto(
                    studentId = currentProgress.studentId,
                    firstName = currentProgress.firstName,
                    middleInitial = currentProgress.middleInitial,
                    surname = currentProgress.surname,
                    gradeLevel = currentProgress.gradeLevel,
                    gender = currentProgress.gender,
                    stars = currentProgress.stars,
                    masteredLetters = currentProgress.masteredLetters,
                    selectedAvatar = currentProgress.selectedAvatar,
                    unlockedLevel = currentProgress.unlockedLevel
                )
                SupabaseClient.api.upsertUserProgress(
                    apiKey = BuildConfig.SUPABASE_ANON_KEY,
                    auth = "Bearer ${BuildConfig.SUPABASE_ANON_KEY}",
                    progress = dto
                )
                // Mark as synced
                userProgressDao.saveProgress(currentProgress.copy(isSynced = true))
            }

            val unsyncedRecords = gameRecordDao.getUnsyncedRecords()
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }

            for (record in unsyncedRecords) {
                val dto = GameRecordDto(
                    studentId = currentProgress?.studentId ?: "unknown",
                    gameName = record.gameName,
                    score = record.score,
                    totalItems = record.totalItems,
                    starsEarned = record.starsEarned,
                    playedAt = sdf.format(Date(record.timestamp))
                )
                SupabaseClient.api.insertGameRecord(
                    apiKey = BuildConfig.SUPABASE_ANON_KEY,
                    auth = "Bearer ${BuildConfig.SUPABASE_ANON_KEY}",
                    record = dto
                )
                // Mark as synced
                gameRecordDao.insertGameRecord(record.copy(isSynced = true))
            }
        } catch (e: Exception) {
            Log.e("SupabaseSync", "Failed to sync to Supabase: ${e.message}")
        }
    }
}
