package com.uade.dist.morfando.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RestaurantModel(
    @SerializedName("code") val code: String,
    @SerializedName("name") val name: String,
    @SerializedName("cookingType") val cookingType: String,
    @SerializedName("priceRange") val priceRange: Int,
    @SerializedName("rating") val rating: Long,
    @SerializedName("neighborhood") val neighborhood: String,
    @SerializedName("image") val image: String,
    @SerializedName("latitude") val latitude: Double = 0.0,
    @SerializedName("longitude") val longitude: Double = 0.0,
    @SerializedName("status") var status: String? = null
): Serializable