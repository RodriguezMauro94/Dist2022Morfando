package com.uade.dist.morfando.domain

import com.uade.dist.morfando.core.RetrofitHelper
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.data.model.SearchFilterOptionsModel
import com.uade.dist.morfando.data.network.RestaurantApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetRestaurantsUseCase {
    private val retrofit = RetrofitHelper.getRetrofit()

    suspend fun getRestaurants(filter: SearchFilterOptionsModel): Result<List<RestaurantModel>> {
        return withContext(Dispatchers.IO) {
            val response = retrofit.create(RestaurantApiClient::class.java).getRestaurants()
            response
        }
    }
}