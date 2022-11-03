package com.uade.dist.morfando.data

import com.uade.dist.morfando.data.model.SessionModel
import com.uade.dist.morfando.data.model.UserModel
import com.uade.dist.morfando.data.network.UserService

class UserRepository {
    private val api = UserService()

    suspend fun authenticate(user: UserModel): SessionModel {
        return api.authenticate(user)
    }

    suspend fun register(user: UserModel): UserModel {
        return api.register(user)
    }
}