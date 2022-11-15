package com.uade.dist.morfando.data.network

import com.uade.dist.morfando.data.model.PersonalDataModel
import com.uade.dist.morfando.data.model.SessionModel
import com.uade.dist.morfando.data.model.UserModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UserApiClient {
    @POST("login/authenticate")
    suspend fun authenticate(@Body user: UserModel): Result<SessionModel>

    @POST("register")
    suspend fun register(@Body user: UserModel): Result<SessionModel>

    @GET("users")
    suspend fun getPersonalData(): Result<PersonalDataModel>

    @PATCH("users")
    suspend fun updatePersonalData(@Body personalDataModel: PersonalDataModel): Result<PersonalDataModel>

    @PATCH("users/password")
    suspend fun changePassword(@Body newPassword: String): Result<SessionModel>
}