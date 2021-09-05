package com.hyochan.sendbird.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitBuilder {
    private const val baseUrl = "https://api.itbook.store/"
    private const val version = "1.0"

    private const val CONNECT_TIMEOUT = 3L
    private const val READ_TIMEOUT = 3L

    fun getRetrofit(): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(HeaderInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("$baseUrl$version/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}