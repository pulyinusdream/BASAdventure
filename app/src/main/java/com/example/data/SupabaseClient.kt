package com.example.data

import com.example.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object SupabaseClient {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    // We check if the URL is set. If not, it falls back to a placeholder so it won't crash instantly.
    private val baseUrl = if (BuildConfig.SUPABASE_URL.startsWith("http")) {
        BuildConfig.SUPABASE_URL
    } else {
        "https://placeholder.supabase.co/" // Fallback
    }
    // Ensures a trailing slash
    private val finalUrl = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"

    val api: SupabaseApi by lazy {
        Retrofit.Builder()
            .baseUrl(finalUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(SupabaseApi::class.java)
    }
}
