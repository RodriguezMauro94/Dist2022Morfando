package com.uade.dist.morfando.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RatingModel(
    @SerializedName("restaurantCode") val code: String,
    @SerializedName("rating") val rating: Long,
    @SerializedName("comment") val title: String,
    @SerializedName("userCode") val userCode: String? = null,
    @SerializedName("description") val description: String, // FIXME no estan en back
    @SerializedName("userName") val userName: String? = null,
    @SerializedName("image") val userImage: String? = null
): Serializable
