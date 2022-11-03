package com.uade.dist.morfando.domain

import com.uade.dist.morfando.data.RestaurantRepository
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.data.model.SearchFilterOptionsModel

class GetRestaurantsUseCase {
    private val repository = RestaurantRepository()

    suspend fun getAllRestaurants(): List<RestaurantModel> = repository.getAllRestaurants()

    suspend fun getRestaurants(filter: SearchFilterOptionsModel) = repository.getRestaurants(filter)
}