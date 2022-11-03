package com.uade.dist.morfando.domain

import com.uade.dist.morfando.data.UserRepository
import com.uade.dist.morfando.data.model.UserModel

class RegisterUseCase {
    private val repository = UserRepository()

    suspend operator fun invoke(user: UserModel): UserModel = repository.register(user)
}