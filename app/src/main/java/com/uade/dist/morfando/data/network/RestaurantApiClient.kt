package com.uade.dist.morfando.data.network

import com.uade.dist.morfando.data.model.*
import retrofit2.http.*

interface RestaurantApiClient {
    @GET("filter") // FIXME agregar filtros en back
    suspend fun getRestaurants(): Result<List<RestaurantModel>>

    @GET("restaurant/details")
    suspend fun getRestaurantDetails(@Query("restaurant-code") code: String): Result<RestaurantDetailsModel>

    @GET("plates")
    suspend fun getMenu(@Query("restaurant-code") code: String): Result<MenuModel>

    @PATCH("plates")
    suspend fun updateMenu(@Body saveMenu: SaveMenuModel): Result<RestaurantModel>

    @POST("plates")
    suspend fun saveMenu(@Body saveMenu: SaveMenuModel): Result<RestaurantModel>

    @POST("restaurant")
    suspend fun createRestaurant(@Body restaurant: CreateRestaurantModel): Result<RestaurantModel>

    @PATCH("restaurant")
    suspend fun editRestaurant(@Body restaurant: CreateRestaurantModel): Result<RestaurantModel>
}