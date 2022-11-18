package com.uade.dist.morfando.domain

import com.uade.dist.morfando.core.RetrofitHelper
import com.uade.dist.morfando.data.model.CreateRestaurantModel
import com.uade.dist.morfando.data.model.RestaurantModel
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
}