package com.uade.dist.morfando.data.network

import com.uade.dist.morfando.core.RetrofitHelper
import com.uade.dist.morfando.data.model.RestaurantModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RestaurantService {
    private val retrofit = RetrofitHelper.getRetrofit()

    suspend fun getRestaurants(): List<RestaurantModel> {
        return withContext(Dispatchers.IO) {
            val response = retrofit.create(RestaurantApiClient::class.java).getAllRestaurants()
            response.body() ?: emptyList()
        }
    }
}