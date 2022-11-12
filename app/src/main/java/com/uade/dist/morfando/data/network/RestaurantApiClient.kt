package com.uade.dist.morfando.data.network

import com.uade.dist.morfando.data.model.CreateRestaurantModel
import com.uade.dist.morfando.data.model.MenuItemModel
import com.uade.dist.morfando.data.model.RestaurantDetailsModel
import com.uade.dist.morfando.data.model.RestaurantModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RestaurantApiClient {
    @GET("filter") //FIXME agregar filtros en back
    suspend fun getRestaurants(): Result<List<RestaurantModel>>

    @GET("restaurant/details")
    suspend fun getRestaurantDetails(@Query("restaurant-code") code: String): Result<RestaurantDetailsModel>

    @GET("plates")
    suspend fun getMenu(@Query("restaurant-code") code: String): Result<List<MenuItemModel>>

    @POST("restaurant")
    suspend fun createRestaurant(@Body restaurant: CreateRestaurantModel): Result<RestaurantModel>
}