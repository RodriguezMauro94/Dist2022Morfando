package com.uade.dist.morfando.data.network

import com.uade.dist.morfando.data.model.FavoritesUpdateModel
import com.uade.dist.morfando.data.model.PersonalDataModel
import com.uade.dist.morfando.data.model.SessionModel
import com.uade.dist.morfando.data.model.UserModel
import retrofit2.http.*

interface UserApiClient {
    @POST("login/authenticate")
    suspend fun authenticate(@Body user: UserModel): Result<SessionModel>

    @POST("register")
    suspend fun register(@Body user: UserModel): Result<SessionModel>

    @GET("users")
    suspend fun getPersonalData(): Result<PersonalDataModel>

    @PATCH("users")
    suspend fun updatePersonalData(@Body personalDataModel: PersonalDataModel): Result<PersonalDataModel>

    @PATCH("users")
    suspend fun updateFavourites(@Body favourites: FavoritesUpdateModel): Result<SessionModel>

    @PATCH("users/password")
    suspend fun changePassword(@Body newPassword: String): Result<SessionModel>

    @DELETE("users")
    suspend fun deleteAccount(): Result<SessionModel>

    @GET("users/email/{email}/otp")
    suspend fun forgotPassword(@Path(value="email") email: String): Result<SessionModel>

    @GET("users/validate/email/{email}/otp/{otpCode}")
    suspend fun validateOtp(@Path(value="email") email: String, @Path(value="otpCode") otpCode: String): Result<SessionModel>
}