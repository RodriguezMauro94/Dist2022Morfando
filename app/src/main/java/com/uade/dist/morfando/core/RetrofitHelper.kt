package com.uade.dist.morfando.core

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitHelper {
    fun getRetrofit(token: String? = null): Retrofit {
        val logger = HttpLoggingInterceptor()
        logger.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder().addInterceptor(logger)

        if (token != null) {
            client.addInterceptor(Interceptor { chain ->
                val request: Request =
                    chain.request().newBuilder().addHeader(
                        "Authorization",
                        "Bearer $token"
                    ).build()
                chain.proceed(request)
            })
        }

        return Retrofit.Builder()
            .baseUrl("https://morfando-app.herokuapp.com/morfando-back/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .client(client.build())
            .build()
    }
}