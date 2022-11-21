package com.uade.dist.morfando.domain

import com.uade.dist.morfando.core.RetrofitHelper
import com.uade.dist.morfando.data.model.CreateRestaurantModel
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.data.model.SessionModel
import com.uade.dist.morfando.data.network.RestaurantApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EditRestaurantUseCase {
    suspend fun editRestaurant(auth: String, restaurant: CreateRestaurantModel): Result<RestaurantModel> {
        val retrofit = RetrofitHelper.getRetrofit(auth)
        val api = retrofit.create(RestaurantApiClient::class.java)
        return withContext(Dispatchers.IO) {
            api.editRestaurant(restaurant)
        }
    }

    suspend fun deleteRestaurant(auth: String, code: String): Result<SessionModel> {
        val retrofit = RetrofitHelper.getRetrofit(auth)
        val api = retrofit.create(RestaurantApiClient::class.java)
        return withContext(Dispatchers.IO) {
            api.deleteRestaurant(code)
        }
    }

    suspend fun updateStatus(auth: String, code: String, status: String): Result<RestaurantModel> {
        val retrofit = RetrofitHelper.getRetrofit(auth)
        val api = retrofit.create(RestaurantApiClient::class.java)
        return withContext(Dispatchers.IO) {
            api.updateStatus(code, status)
        }
    }
}