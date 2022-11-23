package com.uade.dist.morfando.domain

import com.uade.dist.morfando.core.RetrofitHelper
import com.uade.dist.morfando.data.model.*
import com.uade.dist.morfando.data.network.RestaurantApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetRestaurantsUseCase(auth: String) {
    private val retrofit = RetrofitHelper.getRetrofit(auth)
    private val api = retrofit.create(RestaurantApiClient::class.java)

    suspend fun getRestaurants(filter: SearchFilterOptionsModel): Result<List<RestaurantModel>> {
        return withContext(Dispatchers.IO) {
            api.getRestaurants(filter.openNow, filter.priceRange, filter.ratingRange, filter.cookingType, filter.distance)
        }
    }

    suspend fun getRestaurantDetail(code: String): Result<RestaurantDetailsModel> {
        return withContext(Dispatchers.IO) {
            api.getRestaurantDetails(code)
        }
    }

    suspend fun getMenu(code: String): Result<MenuModel> {
        return withContext(Dispatchers.IO) {
            api.getMenu(code)
        }
    }

    suspend fun getFavourites(): Result<List<RestaurantModel>> {
        return withContext(Dispatchers.IO) {
            api.getFavourites()
        }
    }

    suspend fun getMyRestaurants(): Result<List<RestaurantModel>> {
        return withContext(Dispatchers.IO) {
            api.getMyRestaurants()
        }
    }
}