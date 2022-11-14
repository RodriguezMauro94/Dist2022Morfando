package com.uade.dist.morfando.domain

import com.uade.dist.morfando.core.RetrofitHelper
import com.uade.dist.morfando.data.model.PersonalDataModel
import com.uade.dist.morfando.data.network.UserApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserPersonalDataUseCase {
    private val retrofit = RetrofitHelper.getRetrofit()
    private val api = retrofit.create(UserApiClient::class.java)

    suspend fun getPersonalData(): Result<PersonalDataModel> {
        return withContext(Dispatchers.IO) {
            val response = api.getPersonalData()
            response
        }
    }

    suspend fun updatePersonalData(personalDataModel: PersonalDataModel): Result<PersonalDataModel> {
        return withContext(Dispatchers.IO) {
            val response = api.updatePersonalData(personalDataModel)
            response
        }
    }
}