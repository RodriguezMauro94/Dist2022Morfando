package com.uade.dist.morfando.data.network

import com.uade.dist.morfando.data.model.SessionModel
import com.uade.dist.morfando.data.model.UserModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApiClient {
    @POST("login/authenticate")
    suspend fun authenticate(@Body user: UserModel): Response<SessionModel>

    @POST("register")
    suspend fun register(@Body user: UserModel)
}