import re

with open('app/src/main/java/com/example/data/SupabaseApi.kt', 'r') as f:
    content = f.read()

if 'import retrofit2.http.Query' not in content:
    content = content.replace('import retrofit2.http.POST', 'import retrofit2.http.POST\nimport retrofit2.http.Query')

old_post = '''    @POST("rest/v1/user_progress")
    suspend fun upsertUserProgress(
        @Header("apikey") apiKey: String,
        @Header("Authorization") auth: String,
        @Header("Prefer") prefer: String = "resolution=merge-duplicates",
        @Body progress: UserProgressDto
    )'''

new_post = '''    @POST("rest/v1/user_progress")
    suspend fun upsertUserProgress(
        @Header("apikey") apiKey: String,
        @Header("Authorization") auth: String,
        @Header("Prefer") prefer: String = "resolution=merge-duplicates",
        @Query("on_conflict") onConflict: String = "student_id",
        @Body progress: UserProgressDto
    )'''

content = content.replace(old_post, new_post)

with open('app/src/main/java/com/example/data/SupabaseApi.kt', 'w') as f:
    f.write(content)
