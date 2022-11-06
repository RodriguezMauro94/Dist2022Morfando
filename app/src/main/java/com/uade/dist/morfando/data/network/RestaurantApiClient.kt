package com.uade.dist.morfando.data.network

import com.uade.dist.morfando.data.model.RestaurantDetailsModel
import com.uade.dist.morfando.data.model.RestaurantModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RestaurantApiClient {
    @GET("filter") //FIXME agregar filtros en back
    suspend fun getRestaurants(): Result<List<RestaurantModel>>

    @POST("restaurant/details")
    suspend fun getRestaurantDetails(@Body code: String): Result<RestaurantDetailsModel>
}