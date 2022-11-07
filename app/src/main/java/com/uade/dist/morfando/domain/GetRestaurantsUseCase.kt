package com.uade.dist.morfando.domain

import com.uade.dist.morfando.core.RetrofitHelper
import com.uade.dist.morfando.data.model.MenuItemModel
import com.uade.dist.morfando.data.model.RestaurantDetailsModel
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.data.model.SearchFilterOptionsModel
import com.uade.dist.morfando.data.network.RestaurantApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetRestaurantsUseCase {
    private val retrofit = RetrofitHelper.getRetrofit()
    private val api = retrofit.create(RestaurantApiClient::class.java)

    suspend fun getRestaurants(filter: SearchFilterOptionsModel): Result<List<RestaurantModel>> {
        return withContext(Dispatchers.IO) {
            api.getRestaurants()
        }
    }

    suspend fun getRestaurantDetail(code: String): Result<RestaurantDetailsModel> {
        return withContext(Dispatchers.IO) {
            api.getRestaurantDetails(code)
        }
    }

    suspend fun getMenu(code: String): Result<List<MenuItemModel>> {
        return withContext(Dispatchers.IO) {
            api.getMenu(code)
        }
    }
}