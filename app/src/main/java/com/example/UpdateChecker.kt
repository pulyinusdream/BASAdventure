package com.example

import android.content.Context
import android.content.Intent
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class AppUpdateInfo(
    val versionCode: Int,
    val versionName: String,
    val releaseNotes: String,
    val downloadUrl: String
)

object UpdateChecker {
    // Palitan ito ng iyong JSON endpoint URL o GitHub API
    // Halimbawa: "https://raw.githubusercontent.com/username/repo/main/update.json"
    private const val UPDATE_URL = "https://gist.githubusercontent.com/dreiDdeveye/7a3ea6b4e4b70d263148d2d74e4b696a/raw/update.json"

    suspend fun checkForUpdates(): AppUpdateInfo? {
        return withContext(Dispatchers.IO) {
            try {
                val connection = URL(UPDATE_URL).openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val json = JSONObject(response)
                    
                    val versionCode = json.getInt("versionCode")
                    val versionName = json.getString("versionName")
                    val releaseNotes = json.optString("releaseNotes", "Mayroong bagong update na available!")
                    val downloadUrl = json.getString("downloadUrl")

                    // Kung ang versionCode sa server ay mas mataas sa current app's version code
                    // (Note: Sa Kotlin/Android, dapat mong i-compare ito sa BuildConfig.VERSION_CODE)
                    if (versionCode > BuildConfig.VERSION_CODE) {
                        return@withContext AppUpdateInfo(versionCode, versionName, releaseNotes, downloadUrl)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            null
        }
    }

    fun openDownloadLink(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
