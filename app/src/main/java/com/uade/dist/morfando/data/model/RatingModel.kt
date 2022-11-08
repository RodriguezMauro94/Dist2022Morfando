package com.uade.dist.morfando.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RatingModel(
    @SerializedName("userCode") val user: String,
    @SerializedName("restaurantCode") val code: String,
    @SerializedName("rating") val rating: Long,
    @SerializedName("comment") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("image") val userImage: String? = null
): Serializable
