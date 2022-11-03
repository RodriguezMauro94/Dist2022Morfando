package com.uade.dist.morfando.data.network

import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.data.model.SearchFilterOptionsModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET

interface RestaurantApiClient {
    @GET("restaurants")
    suspend fun getAllRestaurants(): Response<List<RestaurantModel>>

    @GET("filter") //FIXME agregar filtros en back, esto debería ser una lista arreglar en el back
    suspend fun getRestaurants(): Response<RestaurantModel>
}