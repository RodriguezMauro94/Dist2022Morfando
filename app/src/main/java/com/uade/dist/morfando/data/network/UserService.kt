package com.uade.dist.morfando.data.network

import com.uade.dist.morfando.core.RetrofitHelper
import com.uade.dist.morfando.data.model.SessionModel
import com.uade.dist.morfando.data.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserService {
    private val retrofit = RetrofitHelper.getRetrofit()

    suspend fun authenticate(user: UserModel): SessionModel {
        return withContext(Dispatchers.IO) {
            val response = retrofit.create(UserApiClient::class.java).authenticate(user)
            response.body()!!
        }
    }

    suspend fun register(user: UserModel): UserModel {
        return withContext(Dispatchers.IO) {
            retrofit.create(UserApiClient::class.java).register(user)
            user
        }
    }
}