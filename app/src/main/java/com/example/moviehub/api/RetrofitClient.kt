package com.example.moviehub.api

import com.example.moviehub.utilities.Constants.Companion.API_KEY
import com.example.moviehub.utilities.Constants.Companion.BASE_URL
import com.example.moviehub.utilities.Constants.Companion.BEARER_TOKEN
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient private constructor() {

    private val requestInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url()
        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("api_key", API_KEY)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .addHeader("Authorization", "Bearer $BEARER_TOKEN")
            .build()

        return@Interceptor chain.proceed(newRequest)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(requestInterceptor)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    companion object {
        private var instance: RetrofitClient? = null

        fun getInstance(): RetrofitClient {
            if (instance == null) {
                instance = RetrofitClient()
            }
            return instance!!
        }
    }

    fun getClient(): Retrofit {
        return retrofit
    }
}