package com.uade.dist.morfando.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RatingModel(
    @SerializedName("user") val user: String,
    @SerializedName("rating") val rating: Long,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("image") val userImage: String
): Serializable
