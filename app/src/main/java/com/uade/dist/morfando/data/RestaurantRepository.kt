package com.uade.dist.morfando.data

import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.data.model.RestaurantProvider
import com.uade.dist.morfando.data.model.SearchFilterOptionsModel
import com.uade.dist.morfando.data.network.RestaurantService

class RestaurantRepository {
    private val api = RestaurantService()

    suspend fun getAllRestaurants(): List<RestaurantModel> {
        val response = api.getRestaurants()
        RestaurantProvider.restaurants = response
        return response
    }

    suspend fun getRestaurants(filter: SearchFilterOptionsModel): List<RestaurantModel> {
        val response = api.getRestaurants(filter)
        RestaurantProvider.restaurants = response
        return response
    }
}