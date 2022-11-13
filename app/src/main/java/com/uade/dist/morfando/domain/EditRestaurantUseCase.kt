package com.uade.dist.morfando.domain

import com.uade.dist.morfando.core.RetrofitHelper
import com.uade.dist.morfando.data.model.CreateRestaurantModel
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.data.network.RestaurantApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EditRestaurantUseCase {
    private val retrofit = RetrofitHelper.getRetrofit()
    private val api = retrofit.create(RestaurantApiClient::class.java)

    suspend fun editRestaurant(restaurant: CreateRestaurantModel): Result<RestaurantModel> {
        return withContext(Dispatchers.IO) {
            api.editRestaurant(restaurant)
        }
    }
}