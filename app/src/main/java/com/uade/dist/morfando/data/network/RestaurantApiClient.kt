package com.uade.dist.morfando.data.network

import com.uade.dist.morfando.data.model.RestaurantModel
import retrofit2.Response
import retrofit2.http.GET

interface RestaurantApiClient {
    @GET("restaurants")
    suspend fun getAllRestaurants(): Response<List<RestaurantModel>>
}