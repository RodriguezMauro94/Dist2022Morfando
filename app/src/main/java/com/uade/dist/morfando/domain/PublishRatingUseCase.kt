package com.uade.dist.morfando.domain

import com.uade.dist.morfando.core.RetrofitHelper
import com.uade.dist.morfando.data.model.RatingModel
import com.uade.dist.morfando.data.network.RatingApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PublishRatingUseCase {
    suspend fun publish(auth: String, rating: RatingModel): Result<Any> {
        val retrofit = RetrofitHelper.getRetrofit(auth, rating.code)
        return withContext(Dispatchers.IO) {
            val response = retrofit.create(RatingApiClient::class.java).publish(rating)
            response
        }
    }
}