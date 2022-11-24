package com.uade.dist.morfando.core

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    fun getRetrofit(token: String? = null, restaurantCode: String? = null): Retrofit {
        val logger = HttpLoggingInterceptor()
        logger.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder().addInterceptor(logger)

        client.addInterceptor(Interceptor { chain ->
            val builder = chain.request().newBuilder()
            if (restaurantCode != null) {
                builder.addHeader(
                    "x-restaurant-code",
                    restaurantCode
                )
            }
            if (token != null) {
                builder.addHeader(
                    "Authorization",
                    "Bearer $token"
                )
            }

            chain.proceed(builder.build())
        })

        return Retrofit.Builder()
            .baseUrl("https://morfando-app.herokuapp.com/morfando-back/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .client(client.build())
            .build()
    }
}