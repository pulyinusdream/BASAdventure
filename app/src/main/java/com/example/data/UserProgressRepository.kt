package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import android.util.Log
import com.example.BuildConfig
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class UserProgressRepository(
    private val userProgressDao: UserProgressDao,
    private val gameRecordDao: GameRecordDao,
    private val examRecordDao: ExamRecordDao
) {
    val progress: Flow<UserProgress?> = userProgressDao.getProgress()
    val allGameRecords: Flow<List<GameRecord>> = gameRecordDao.getAllGameRecords()
    val allExamRecords: Flow<List<ExamRecord>> = examRecordDao.getAllExamRecords()

    suspend fun saveProgress(progress: UserProgress) {
        // Mark as unsynced whenever we save
        userProgressDao.saveProgress(progress.copy(isSynced = false))
        syncPendingData()
    }

    suspend fun saveGameRecord(record: GameRecord) {
        gameRecordDao.insertGameRecord(record.copy(isSynced = false))
        syncPendingData()
    }

    suspend fun saveExamRecord(record: ExamRecord) {
        examRecordDao.insertExamRecord(record.copy(isSynced = false))
        syncPendingData()
    }

    private suspend fun syncPendingData() {
        if (!BuildConfig.SUPABASE_URL.startsWith("http") || BuildConfig.SUPABASE_ANON_KEY.isEmpty()) {
            return // Skip if no valid config
        }

        try {
            var currentProgress = userProgressDao.getProgressSync()
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
                try {
                    SupabaseClient.api.upsertUserProgress(
                        apiKey = BuildConfig.SUPABASE_ANON_KEY,
                        auth = "Bearer ${BuildConfig.SUPABASE_ANON_KEY}",
                        progress = dto
                    )
                    // Mark as synced locally
                    currentProgress = currentProgress.copy(isSynced = true)
                    userProgressDao.saveProgress(currentProgress)
                    Log.d("SupabaseSync", "Profile synced successfully")
                } catch (e: Exception) {
                    Log.e("SupabaseSync", "Profile sync failed: ${e.message}")
                    return // Stop sync if profile fails (critical for foreign keys)
                }
            }

            if (currentProgress == null || !currentProgress.isSynced) {
                Log.w("SupabaseSync", "Skipping records sync: Profile not synced yet")
                return
            }

            val studentId = currentProgress.studentId
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }

            // Sync Game Records
            val unsyncedGameRecords = gameRecordDao.getUnsyncedRecords()
            for (record in unsyncedGameRecords) {
                val dto = GameRecordDto(
                    studentId = studentId,
                    gameName = record.gameName,
                    score = record.score,
                    totalItems = record.totalItems,
                    starsEarned = record.starsEarned,
                    playedAt = sdf.format(Date(record.timestamp))
                )
                try {
                    SupabaseClient.api.insertGameRecord(
                        apiKey = BuildConfig.SUPABASE_ANON_KEY,
                        auth = "Bearer ${BuildConfig.SUPABASE_ANON_KEY}",
                        record = dto
                    )
                    gameRecordDao.insertGameRecord(record.copy(isSynced = true))
                } catch (e: Exception) {
                    Log.e("SupabaseSync", "Game record sync failed: ${e.message}")
                }
            }

            // Sync Exam Records
            val unsyncedExamRecords = examRecordDao.getUnsyncedRecords()
            if (unsyncedExamRecords.isNotEmpty()) {
                Log.d("SupabaseSync", "Found ${unsyncedExamRecords.size} unsynced exam records")
                for (record in unsyncedExamRecords) {
                    val dto = ExamRecordDto(
                        studentId = studentId,
                        examName = record.examName,
                        score = record.score,
                        totalItems = record.totalItems,
                        timestamp = record.timestamp
                    )
                    try {
                        SupabaseClient.api.insertExamRecord(
                            apiKey = BuildConfig.SUPABASE_ANON_KEY,
                            auth = "Bearer ${BuildConfig.SUPABASE_ANON_KEY}",
                            record = dto
                        )
                        examRecordDao.markAsSynced(record.id)
                        Log.d("SupabaseSync", "Successfully synced exam: ${record.examName}")
                    } catch (e: Exception) {
                        Log.e("SupabaseSync", "Error syncing exam ${record.examName}: ${e.localizedMessage}")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("SupabaseSync", "General sync failure: ${e.message}")
        }
    }
}
