package com.uade.dist.morfando.data.network

import com.uade.dist.morfando.data.model.RestaurantModel
import retrofit2.http.GET

interface RestaurantApiClient {
    @GET("restaurants")
    suspend fun getAllRestaurants(): Result<List<RestaurantModel>>

    @GET("filter") //FIXME agregar filtros en back
    suspend fun getRestaurants(): Result<List<RestaurantModel>>
}