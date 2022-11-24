package com.uade.dist.morfando.data.network

import com.uade.dist.morfando.data.model.*
import retrofit2.http.*

interface RestaurantApiClient {
    @GET("filter")
    suspend fun getRestaurants(
        @Query("openNow") openNow: Boolean?,
        @Query("priceRange") priceRange: Int?,
        @Query("ratingRange") ratingRange: Int?,
        @Query("cookingType") cookingType: String?,
        @Query("distance") distance: Int?
    ): Result<List<RestaurantModel>>

    @GET("restaurant")
    suspend fun getMyRestaurants(): Result<List<RestaurantModel>>

    @GET("users/favorites")
    suspend fun getFavourites(): Result<List<RestaurantModel>>

    @GET("restaurant")
    suspend fun getRestaurantDetails(@Query("restaurant-code") code: String): Result<List<RestaurantDetailsModel>>

    @GET("plates")
    suspend fun getMenu(@Query("restaurant-code") code: String): Result<MenuModel>

    @PATCH("menu")
    suspend fun updateMenu(@Body menu: MenuModel): Result<List<MenuModel>>

    @POST("menu")
    suspend fun saveMenu(@Body menu: MenuModel): Result<List<MenuModel>>

    @POST("restaurant")
    suspend fun createRestaurant(@Body restaurant: CreateRestaurantModel): Result<RestaurantModel>

    @PATCH("restaurant")
    suspend fun editRestaurant(@Body restaurant: CreateRestaurantModel): Result<RestaurantModel>

    @DELETE("restaurant")
    suspend fun deleteRestaurant(): Result<SessionModel>

    @PATCH("restaurant")
    suspend fun updateStatus(@Query("restaurant-code") code: String, @Query("status") status: String): Result<RestaurantModel>
}