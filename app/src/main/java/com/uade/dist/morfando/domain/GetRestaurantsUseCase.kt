package com.uade.dist.morfando.domain

import com.uade.dist.morfando.data.RestaurantRepository
import com.uade.dist.morfando.data.model.RestaurantModel

class GetRestaurantsUseCase {
    private val repository = RestaurantRepository()

    suspend operator fun invoke(): List<RestaurantModel> = repository.getAllRestaurants()
}