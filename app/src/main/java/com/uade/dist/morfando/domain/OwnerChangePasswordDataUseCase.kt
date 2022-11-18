package com.uade.dist.morfando.domain

import com.uade.dist.morfando.core.RetrofitHelper
import com.uade.dist.morfando.data.model.PersonalDataModel
import com.uade.dist.morfando.data.model.SessionModel
import com.uade.dist.morfando.data.network.UserApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OwnerChangePasswordDataUseCase {
    suspend fun changePassword(auth: String, newPassword: String): Result<SessionModel> {
        val retrofit = RetrofitHelper.getRetrofit(auth)
        val api = retrofit.create(UserApiClient::class.java)
        return withContext(Dispatchers.IO) {
            val response = api.changePassword(newPassword)
            response
        }
    }
}