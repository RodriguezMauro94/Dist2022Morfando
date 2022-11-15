package com.uade.dist.morfando.domain

import com.uade.dist.morfando.core.RetrofitHelper
import com.uade.dist.morfando.data.model.PersonalDataModel
import com.uade.dist.morfando.data.model.SessionModel
import com.uade.dist.morfando.data.network.UserApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OwnerChangePasswordDataUseCase {
    private val retrofit = RetrofitHelper.getRetrofit()
    private val api = retrofit.create(UserApiClient::class.java)

    suspend fun changePassword(newPassword: String): Result<SessionModel> {
        return withContext(Dispatchers.IO) {
            val response = api.changePassword(newPassword)
            response
        }
    }
}