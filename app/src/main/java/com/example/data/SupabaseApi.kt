package com.example.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Query

@JsonClass(generateAdapter = true)
data class UserProgressDto(
    @Json(name = "student_id") val studentId: String,
    @Json(name = "first_name") val firstName: String,
    @Json(name = "middle_initial") val middleInitial: String,
    @Json(name = "surname") val surname: String,
    @Json(name = "grade_level") val gradeLevel: String,
    @Json(name = "gender") val gender: String,
    @Json(name = "stars") val stars: Int,
    @Json(name = "mastered_letters") val masteredLetters: String,
    @Json(name = "selected_avatar") val selectedAvatar: String,
    @Json(name = "unlocked_level") val unlockedLevel: Int,
    @Json(name = "profile_image") val profileImage: String? = null
)

@JsonClass(generateAdapter = true)
data class GameRecordDto(
    @Json(name = "student_id") val studentId: String,
    @Json(name = "game_name") val gameName: String,
    @Json(name = "score") val score: Int,
    @Json(name = "total_items") val totalItems: Int,
    @Json(name = "stars_earned") val starsEarned: Int,
    @Json(name = "played_at") val playedAt: String
)

@JsonClass(generateAdapter = true)
data class ExamRecordDto(
    @Json(name = "student_id") val studentId: String,
    @Json(name = "exam_name") val examName: String,
    @Json(name = "score") val score: Int,
    @Json(name = "total_items") val totalItems: Int,
    @Json(name = "timestamp") val timestamp: Long
)

interface SupabaseApi {
    @POST("rest/v1/user_progress")
    suspend fun upsertUserProgress(
        @Header("apikey") apiKey: String,
        @Header("Authorization") auth: String,
        @Header("Prefer") prefer: String = "resolution=merge-duplicates",
        @Query("on_conflict") onConflict: String = "student_id",
        @Body progress: UserProgressDto
    )

    @POST("rest/v1/game_records")
    suspend fun insertGameRecord(
        @Header("apikey") apiKey: String,
        @Header("Authorization") auth: String,
        @Body record: GameRecordDto
    )

    @POST("rest/v1/exam_records")
    suspend fun insertExamRecord(
        @Header("apikey") apiKey: String,
        @Header("Authorization") auth: String,
        @Body record: ExamRecordDto
    )
}
