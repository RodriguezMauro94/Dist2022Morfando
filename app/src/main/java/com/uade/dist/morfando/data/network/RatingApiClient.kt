package com.uade.dist.morfando.data.network

import com.uade.dist.morfando.data.model.RatingModel
import retrofit2.http.Body
import retrofit2.http.POST

interface RatingApiClient {
    @POST("reviews")
    suspend fun publish(@Body rating: RatingModel): Result<Any>

    @POST("reviews")
    suspend fun getReviews(): Result<List<RatingModel>>
}