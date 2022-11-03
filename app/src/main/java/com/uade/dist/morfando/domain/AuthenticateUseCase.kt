package com.uade.dist.morfando.domain

import com.uade.dist.morfando.data.UserRepository
import com.uade.dist.morfando.data.model.SessionModel
import com.uade.dist.morfando.data.model.UserModel

class AuthenticateUseCase {
    private val repository = UserRepository()

    suspend operator fun invoke(user: UserModel): SessionModel = repository.authenticate(user)
}